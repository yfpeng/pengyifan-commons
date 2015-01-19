package com.pengyifan.kernel.svm;

import java.io.File;
import java.io.IOException;

import libsvm.svm;
import libsvm.svm_parameter;
import libsvm.svm_print_interface;

@Deprecated
public class LibSVMTrainerClient {

  private svm_parameter param;
  private String inputFileName;
  private String modelFileName;
  private int crossValidation;
  private int nrFold;

  private static void exitWithHelp() {
    System.out
        .println("Usage: svm_train [options] training_set_file [model_file]\n"
            + "options:\n"
            + "-s svm_type : set type of SVM (default 0)\n"
            + "  0 -- C-SVC    (multi-class classification)\n"
            + "  1 -- nu-SVC   (multi-class classification)\n"
            + "  2 -- one-class SVM\n"
            + "  3 -- epsilon-SVR  (regression)\n"
            + "  4 -- nu-SVR   (regression)\n"
            + "-t kernel_type : set type of kernel function (default 2)\n"
            + "  0 -- linear: u'*v\n"
            + "  1 -- polynomial: (gamma*u'*v + coef0)^degree\n"
            + "  2 -- radial basis function: exp(-gamma*|u-v|^2)\n"
            + "  3 -- sigmoid: tanh(gamma*u'*v + coef0)\n"
            + "  4 -- precomputed kernel (kernel values in training_set_file)\n"
            + "-d degree : set degree in kernel function (default 3)\n"
            + "-g gamma : set gamma in kernel function (default 1/num_features)\n"
            + "-r coef0 : set coef0 in kernel function (default 0)\n"
            + "-c cost : set the parameter C of C-SVC, epsilon-SVR, and nu-SVR (default 1)\n"
            + "-n nu : set the parameter nu of nu-SVC, one-class SVM, and nu-SVR (default 0.5)\n"
            + "-p epsilon : set the epsilon in loss function of epsilon-SVR (default 0.1)\n"
            + "-m cachesize : set cache memory size in MB (default 100)\n"
            + "-e epsilon : set tolerance of termination criterion (default 0.001)\n"
            + "-h shrinking : whether to use the shrinking heuristics, 0 or 1 (default 1)\n"
            + "-b probability_estimates : whether to train a SVC or SVR model for probability estimates, 0 or 1 (default 0)\n"
            + "-wi weight : set the parameter C of class i to weight*C, for C-SVC (default 1)\n"
            + "-v n : n-fold cross validation mode\n"
            + "-q : quiet mode (no outputs)");
    System.exit(1);
  }

  private void doMain(String[] args)
      throws IOException {
    parseCommandLine(args);
    LibSVMTrainer trainer = new LibSVMTrainer(new File(inputFileName),
        new File(modelFileName), param, crossValidation == 1 ? true : false,
        nrFold, false);
    trainer.train();
  }

  public static void main(String[] args)
      throws IOException {
    LibSVMTrainerClient t = new LibSVMTrainerClient();
    t.doMain(args);
  }

  private static double atof(String s) {
    double d = Double.valueOf(s).doubleValue();
    if (Double.isNaN(d) || Double.isInfinite(d)) {
      System.err.print("NaN or Infinity in input\n");
      System.exit(1);
    }
    return (d);
  }

  private void parseCommandLine(String[] argv) {
    svm_print_interface print_func = null; // default printing to stdout
    int i;
    param = new svm_parameter();
    // default values
    param.svm_type = svm_parameter.C_SVC;
    param.kernel_type = svm_parameter.RBF;
    param.degree = 3;
    param.gamma = 0; // 1/num_features
    param.coef0 = 0;
    param.nu = 0.5;
    param.cache_size = 100;
    param.C = 1;
    param.eps = 1e-3;
    param.p = 0.1;
    param.shrinking = 1;
    param.probability = 0;
    param.nr_weight = 0;
    param.weight_label = new int[0];
    param.weight = new double[0];
    crossValidation = 0;

    // parse options
    for (i = 0; i < argv.length; i++) {
      if (argv[i].charAt(0) != '-') {
        break;
      }
      if (++i >= argv.length) {
        exitWithHelp();
      }
      switch (argv[i - 1].charAt(1)) {
      case 's':
        param.svm_type = Integer.parseInt(argv[i]);
        break;
      case 't':
        param.kernel_type = Integer.parseInt(argv[i]);
        break;
      case 'd':
        param.degree = Integer.parseInt(argv[i]);
        break;
      case 'g':
        param.gamma = atof(argv[i]);
        break;
      case 'r':
        param.coef0 = atof(argv[i]);
        break;
      case 'n':
        param.nu = atof(argv[i]);
        break;
      case 'm':
        param.cache_size = atof(argv[i]);
        break;
      case 'c':
        param.C = atof(argv[i]);
        break;
      case 'e':
        param.eps = atof(argv[i]);
        break;
      case 'p':
        param.p = atof(argv[i]);
        break;
      case 'h':
        param.shrinking = Integer.parseInt(argv[i]);
        break;
      case 'b':
        param.probability = Integer.parseInt(argv[i]);
        break;
      case 'q':
        print_func = new svm_print_interface() {

          public void print(String s) {
          }
        };
        i--;
        break;
      case 'v':
        crossValidation = 1;
        nrFold = Integer.parseInt(argv[i]);
        if (nrFold < 2) {
          System.err.print("n-fold cross validation: n must >= 2\n");
          exitWithHelp();
        }
        break;
      case 'w':
        ++param.nr_weight;
        int[] oldWeightLabel = param.weight_label;
        param.weight_label = new int[param.nr_weight];
        System.arraycopy(
            oldWeightLabel,
            0,
            param.weight_label,
            0,
            param.nr_weight - 1);

        double[] oldWeigth = param.weight;
        param.weight = new double[param.nr_weight];
        System.arraycopy(oldWeigth, 0, param.weight, 0, param.nr_weight - 1);

        param.weight_label[param.nr_weight - 1] = Integer.parseInt(argv[i - 1]
            .substring(2));
        param.weight[param.nr_weight - 1] = atof(argv[i]);
        break;
      default:
        System.err.print("Unknown option: " + argv[i - 1] + "\n");
        exitWithHelp();
      }
    }

    svm.svm_set_print_string_function(print_func);

    // determine filenames

    if (i >= argv.length) {
      exitWithHelp();
    }

    inputFileName = argv[i];

    if (i < argv.length - 1) {
      modelFileName = argv[i + 1];
    } else {
      int p = argv[i].lastIndexOf('/');
      ++p; // whew...
      modelFileName = argv[i].substring(p) + ".model";
    }
  }
}
