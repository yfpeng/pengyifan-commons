package com.pengyifan.kernel.svm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.ParserProperties;

/**
 * @deprecated Not really useful.
 */
@Deprecated
public class LibSVMPredictClient {

  @Option(name = "-b", usage = "whether to predict probability estimates, 0 or "
      + "1 (default 0); one-class SVM not supported yet")
  private int probabilityEstimates = 0;

  @Option(name = "-q", usage = "quiet mode (no outputs)")
  private boolean quiteMode = false;

  @Argument
  private List<String> arguments = new ArrayList<>();

  private static final String USAGE =
      "usage: svm_predict [options] test_file model_file output_file";

  public void doMain(String[] args)
      throws IOException {
    // parse options
    ParserProperties.defaults().withUsageWidth(80);
    CmdLineParser parser = new CmdLineParser(this);

    try {
      parser.parseArgument(args);
    } catch (CmdLineException e) {
      System.err.println(USAGE);
      parser.printUsage(System.err);
      System.err.println();
      return;
    }

    if (arguments.size() != 3) {
      System.err.println(USAGE);
      parser.printUsage(System.err);
      System.err.println();
      return;
    }

    LibSVMPredict predict = new LibSVMPredict(
        new File(arguments.get(0)),
        new File(arguments.get(2)),
        new File(arguments.get(1)),
        probabilityEstimates,
        quiteMode
        );
    predict.predict();
  }

  public static void main(String[] args)
      throws IOException {
    LibSVMPredictClient predict = new LibSVMPredictClient();
    predict.doMain(args);
  }
}
