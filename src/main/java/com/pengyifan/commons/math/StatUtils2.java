package com.pengyifan.commons.math;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import static com.google.common.base.Preconditions.checkArgument;

public class StatUtils2 {

  private StatUtils2() throws InstantiationException {
    throw new InstantiationException("This class is not for instantiation");
  }

  public static double mean(double[] data, double[] weights) {
    checkArgument(data.length == weights.length);
    DescriptiveStatistics stats = new DescriptiveStatistics();
    for (int i = 0; i < data.length; i++) {
      stats.addValue(data[i] * weights[i]);
    }
    return stats.getMean();
  }
}
