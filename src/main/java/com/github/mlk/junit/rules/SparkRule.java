package com.github.mlk.junit.rules;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.rules.ExternalResource;

/**
 * Starts and stops a local Spark instance for integration testing against Apache Spark. For an
 * example of it in action see the <a href="http://tinyurl.com/sparkrule">tests for this class.</a>
 */
public class SparkRule extends ExternalResource {

  private JavaSparkContext sc;

  @Override
  protected void before() throws Throwable {
    SparkConf conf = new SparkConf().setAppName("UNIT_TEST").setMaster("local[4]");
    sc = new JavaSparkContext(conf);
  }

  @Override
  protected void after() {
    sc.close();
  }

  public JavaSparkContext getContext() {
    return sc;
  }

}

