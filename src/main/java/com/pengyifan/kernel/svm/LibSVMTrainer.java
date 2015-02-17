package com.pengyifan.kernel.svm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Logger;

import libsvm.svm;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import org.apache.commons.lang3.Validate;

@Deprecated
public class LibSVMTrainer {

  private static Logger logger = Logger.getAnonymousLogger();

  private final File inputFile;
  private final File modelFile;
  private final svm_parameter param;
  private final boolean crossValidation;
  private final int nrFold;
  private final boolean quiteMode;

  public LibSVMTrainer(
      File inputFile,
      File modelFile,
      svm_parameter param,
      boolean crossValidation,
      int nrFold,
      boolean quiteMode) {
    super();
    this.inputFile = inputFile;
    this.modelFile = modelFile;
    this.param = param;
    this.crossValidation = crossValidation;
    this.nrFold = nrFold;
    this.quiteMode = quiteMode;
  }

  private void printMessage(String format, Object... args) {
    if (!quiteMode) {
      System.out.printf(format, args);
      logger.info(String.format(format, args));
    }
  }

  private void doCrossValidation(svm_problem prob, int nrFold) {
    double[] target = new double[prob.l];
    svm.svm_cross_validation(prob, param, nrFold, target);
    if (param.svm_type == svm_parameter.EPSILON_SVR
        || param.svm_type == svm_parameter.NU_SVR) {
      double totalError = 0;
      double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
      for (int i = 0; i < prob.l; i++) {
        double y = prob.y[i];
        double v = target[i];
        totalError += (v - y) * (v - y);
        sumv += v;
        sumy += y;
        sumvv += v * v;
        sumyy += y * y;
        sumvy += v * y;
      }
      printMessage("Cross Validation Mean squared error = %f\n",
          totalError / prob.l);
      printMessage("Cross Validation Squared correlation coefficient = %f\n",
          calculateCoefficient(prob.l, sumvv, sumvy, sumyy, sumv, sumy));
    } else {
      int totalCorrect = 0;
      for (int i = 0; i < prob.l; i++) {
        if (target[i] == prob.y[i]) {
          ++totalCorrect;
        }
      }
      printMessage("Cross Validation Accuracy = %.2f\n",
          100.0 * totalCorrect / prob.l);
    }
  }

  private double calculateCoefficient(double probl, double sumvv, double sumvy,
      double sumyy, double sumv, double sumy) {
    double d = probl * sumvy - sumv * sumy;
    return (d * d)
        /
        ((probl * sumvv - sumv * sumv) * (probl * sumyy - sumy * sumy));
  }

  public void train()
      throws IOException {
    svm_problem prob = readProblem();
    String errorMsg = svm.svm_check_parameter(prob, param);
    if (errorMsg != null) {
      throw new IllegalStateException("ERROR: " + errorMsg);
    }

    if (crossValidation) {
      doCrossValidation(prob, nrFold);
    } else {
      svm.svm_save_model(
          modelFile.getAbsolutePath(),
          svm.svm_train(prob, param));
    }
  }

  private double atof(String s) {
    double d = Double.parseDouble(s);
    if (Double.isNaN(d) || Double.isInfinite(d)) {
      throw new IllegalArgumentException("NaN or Infinity in input");
    }
    return d;
  }

  private svm_problem readProblem()
      throws IOException {

    Vector<Double> vy = new Vector<Double>();
    Vector<svm_node[]> vx = new Vector<svm_node[]>();
    int maxIndex = 0;

    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
    String line;
    while ((line = reader.readLine()) != null) {
      StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
      vy.addElement(atof(st.nextToken()));
      int m = st.countTokens() / 2;
      svm_node[] x = new svm_node[m];
      for (int j = 0; j < m; j++) {
        x[j] = new svm_node();
        x[j].index = Integer.parseInt(st.nextToken());
        x[j].value = atof(st.nextToken());
      }
      if (m > 0) {
        maxIndex = Math.max(maxIndex, x[m - 1].index);
      }
      vx.addElement(x);
    }
    reader.close();

    svm_problem prob = new svm_problem();
    prob.l = vy.size();
    prob.x = new svm_node[prob.l][];
    for (int i = 0; i < prob.l; i++) {
      prob.x[i] = vx.elementAt(i);
    }
    prob.y = new double[prob.l];
    for (int i = 0; i < prob.l; i++) {
      prob.y[i] = vy.elementAt(i);
    }

    if (param.gamma == 0 && maxIndex > 0) {
      param.gamma = 1.0 / maxIndex;
    }

    if (param.kernel_type == svm_parameter.PRECOMPUTED) {
      for (int i = 0; i < prob.l; i++) {
        Validate.isTrue(prob.x[i][0].index == 0,
            "Wrong kernel matrix: first column must be 0:sample_serial_number");
        Validate.isTrue((int) prob.x[i][0].value > 0
            && (int) prob.x[i][0].value <= maxIndex,
            "Wrong input format: sample_serial_number out of range");
      }
    }
    return prob;
  }
}
