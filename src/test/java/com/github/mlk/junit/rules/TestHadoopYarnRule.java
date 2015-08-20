package com.github.mlk.junit.rules;

import com.github.mlk.junit.rules.helpers.hadoop.BasicTestJob;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TestHadoopYarnRule {
    @Rule
    public HadoopYarnRule subject = new HadoopYarnRule("test-hadoop");

    @Test
    public void whenFileExistsThenReturnTrue() throws Exception {
        subject.copyResource("/hello.txt", "/testfile.txt");

        BasicTestJob job = new BasicTestJob();
        job.setConf(subject.getConfiguration());
        int exitCode = job.run("/hello.txt", "/count");
        System.out.println(exitCode);

        assertThat(subject.exist("/count/_SUCCESS"), is(true));
        String output = subject.read("/count/part-r-00000");
        System.out.println(output);
        Map<String, Integer> map = toMap(output);
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
