package com.github.mlk.junit.rules;

import com.github.mlk.junit.rules.helpers.spark.Sum;
import com.github.mlk.junit.rules.helpers.spark.ToItemCounterPair;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestSparkRule {
    @Rule
    public SparkRule subject = new SparkRule();

    /**
     * "Hello World" spark job that counts the number of each word in a word list.
     */
    @Test
    public void helloWorld() {
        JavaSparkContext sc = subject.getContext();
        Map<String, Integer> content = sc
                .parallelize(Arrays.asList("one", "two", "two", "three", "three", "three"))
                .mapToPair(new ToItemCounterPair())
                .reduceByKey(new Sum())
                .collectAsMap();

        assertThat(content.get("one"),   is(1));
        assertThat(content.get("two"),   is(2));
        assertThat(content.get("three"), is(3));
    }
}

