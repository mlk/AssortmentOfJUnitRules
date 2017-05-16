package com.github.mlk.junit.rules;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.github.mlk.junit.rules.helpers.hadoop.BasicTestJob;
import java.util.HashMap;
import java.util.Map;
import org.junit.Rule;
import org.junit.Test;

public class TestHadoopYarnRule {

  @Rule
  public HadoopYarnRule subject = new HadoopYarnRule("test-hadoop");

  /**
   * "Hello World" YARN job that counts the number of each word in a text file.
   */
  @Test
  public void helloWorld() throws Exception {
    subject.copyResource("/hello.txt", "/testfile.txt");

    BasicTestJob job = new BasicTestJob();
    job.setConf(subject.getConfiguration());
    int exitCode = job.run("/hello.txt", "/count");

    assertThat(exitCode, is(0));
    assertThat(subject.exist("/count/_SUCCESS"), is(true));

    Map<String, Integer> map = toMap(subject.read("/count/part-r-00000"));
    assertThat(map.get("one"), is(1));
    assertThat(map.get("two"), is(2));
    assertThat(map.get("three"), is(3));
  }

  private Map<String, Integer> toMap(String outputAsStr) {
    Map<String, Integer> result = new HashMap<>();
    for (String line : outputAsStr.split("\n")) {
      String[] tokens = line.split("\t");
      result.put(tokens[0], Integer.parseInt(tokens[1]));
    }
    return result;
  }
}
