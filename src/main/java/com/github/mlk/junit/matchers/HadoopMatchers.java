package com.github.mlk.junit.matchers;

import com.github.mlk.junit.rules.HadoopDFSRule;
import org.hamcrest.Matcher;

public class HadoopMatchers {

    public static Matcher<HadoopDFSRule> hasFile(String file) {
        return new HadoopFileExists(file);
    }
}
