package com.pengyifan.commons.math;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

public class SparseRealVector {

	int dimension;
	HashMap<Integer, Double> map;

	public SparseRealVector(int dimension) {
		this.dimension = dimension;
		map = new HashMap<Integer, Double>();
	}

	public void setEntry(int index, double value) {
		map.put(index, value);
	}

	public double getEntry(int index) {
		Double value = map.get(index);
		return value == null ? 0.0 : value;
	}

	public int getDimension() {
		return dimension;
	}

	public double dotProduct(SparseRealVector v) {
		Validate.isTrue(dimension == v.dimension);
		double dotProduct = 0;

		for (Map.Entry<Integer, Double> cell : map.entrySet()) {
			double vv = v.getEntry(cell.getKey());
			if (vv != 0) {
				dotProduct += vv * cell.getValue();
			}
		}

		return dotProduct;
	}
}