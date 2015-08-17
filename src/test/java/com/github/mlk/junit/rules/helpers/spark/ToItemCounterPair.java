package com.github.mlk.junit.rules.helpers.spark;

import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

public class ToItemCounterPair implements PairFunction<String, String, Integer> {
    @Override
    public Tuple2<String, Integer> call(String s)throws Exception{
        return new Tuple2<>(s,1);
    }
}
