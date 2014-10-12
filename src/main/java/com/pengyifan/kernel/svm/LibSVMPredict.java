package com.pengyifan.kernel.svm;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;

import org.apache.commons.lang3.Validate;

public class LibSVMPredict {

  private static final Logger LOGGER = Logger.getAnonymousLogger();

  private final File inputFile;
  private final File outputFile;
  private final File modelFile;
  private final int probabilityEstimates;
  private final boolean quiteMode;

  public LibSVMPredict(
      File inputFile,
      File outputFile,
      File modelFile,
      int probabilityEstimates,
      boolean quiteMode) {
    super();
    this.inputFile = inputFile;
    this.outputFile = outputFile;
    this.modelFile = modelFile;
    this.probabilityEstimates = probabilityEstimates;
    this.quiteMode = quiteMode;
  }

  private void printMessage(String format, Object... args) {
    if (!quiteMode) {
      System.out.printf(format, args);
      LOGGER.info(String.format(format, args));
    }
  }

  private svm_model loadModel()
      throws IOException {
    svm_model model = svm.svm_load_model(modelFile.getAbsolutePath());
    if (probabilityEstimates == 1) {
      if (svm.svm_check_probability_model(model) == 0) {
        throw new IllegalArgumentException(
            "Model does not support probabiliy estimates");
      }
    } else {
      if (svm.svm_check_probability_model(model) != 0) {
        printMessage("Model supports probability estimates, "
            + "but disabled in prediction.\n");
      }
    }
    return model;
  }

  public void predict()
      throws IOException {
    
    Validate.isTrue(modelFile.isFile(), 
        "The model file is invalid: %s", modelFile);
    Validate.isTrue(inputFile.isFile(), 
        "The input file is invalid: %s", inputFile);

    svm_model model = loadModel();

    int svmType = svm.svm_get_svm_type(model);
    int nrClass = svm.svm_get_nr_class(model);
    double[] probEstimates = null;

    DataOutputStream output = new DataOutputStream(new FileOutputStream(
        outputFile));
    if (probabilityEstimates == 1) {
      if (svmType == svm_parameter.EPSILON_SVR
          || svmType == svm_parameter.NU_SVR) {
        printMessage(
            "Prob. model for test data: target value = predicted value + z,\n"
                + "z: Laplace distribution e^(-|z|/sigma)/(2sigma),sigma=%f\n",
            svm.svm_get_svr_probability(model));
      } else {
        int[] labels = new int[nrClass];
        svm.svm_get_labels(model, labels);
        probEstimates = new double[nrClass];
        output.writeBytes("labels");
        for (int j = 0; j < nrClass; j++) {
          output.writeBytes(" " + labels[j]);
        }
        output.writeBytes("\n");
      }
    }

    int correct = 0;
    int total = 0;
    double error = 0;
    double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;

    LineNumberReader reader = new LineNumberReader(new FileReader(inputFile));
    String line;
    while ((line = reader.readLine()) != null) {
      StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
      double target = Double.parseDouble(st.nextToken());
      int m = st.countTokens() / 2;
      svm_node[] x = new svm_node[m];
      for (int j = 0; j < m; j++) {
        x[j] = new svm_node();
        x[j].index = Integer.parseInt(st.nextToken());
        x[j].value = Double.parseDouble(st.nextToken());
      }

      double v;
      if (probabilityEstimates == 1
          && (svmType == svm_parameter.C_SVC
          || svmType == svm_parameter.NU_SVC)) {
        v = svm.svm_predict_probability(model, x, probEstimates);
        output.writeBytes(v + " ");
        for (int j = 0; j < nrClass; j++) {
          output.writeBytes(probEstimates[j] + " ");
        }
        output.writeBytes("\n");
      } else {
        v = svm.svm_predict(model, x);
        output.writeBytes(v + "\n");
      }

      if (v == target) {
        ++correct;
      }
      error += (v - target) * (v - target);
      sumv += v;
      sumy += target;
      sumvv += v * v;
      sumyy += target * target;
      sumvy += v * target;
      ++total;
    }
    reader.close();
    output.close();

    if (svmType == svm_parameter.EPSILON_SVR
        || svmType == svm_parameter.NU_SVR) {
      printMessage("Mean squared error = %f (regression)\n",
          error / total);
      printMessage("Squared correlation coefficient = %f (regression)\n",
          calculateCoefficient(total, sumvv, sumvy, sumyy, sumv, sumy));
    } else {
      printMessage("Accuracy = %.2f%% (%d/%d) (classification)\n",
          (double) correct / total * 100,
          correct,
          total);
    }
  }
  
  private double calculateCoefficient(double total, double sumvv, double sumvy,
      double sumyy, double sumv, double sumy) {
    double d = total * sumvy - sumv * sumy;
    return (d * d)
        /
        ((total * sumvv - sumv * sumv) * (total * sumyy - sumy * sumy));
  }
}
