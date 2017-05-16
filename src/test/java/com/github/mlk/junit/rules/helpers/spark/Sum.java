package com.github.mlk.junit.rules.helpers.spark;

import org.apache.spark.api.java.function.Function2;

public class Sum implements Function2<Integer, Integer, Integer> {

  @Override
  public Integer call(Integer v1, Integer v2) throws Exception {
    return v1 + v2;
  }
}
