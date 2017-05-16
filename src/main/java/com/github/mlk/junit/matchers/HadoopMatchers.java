package com.github.mlk.junit.matchers;

import com.github.mlk.junit.rules.HadoopDFSRule;
import org.hamcrest.Matcher;

public class HadoopMatchers {

    public static Matcher<HadoopDFSRule> exists(String file) {
        return new HadoopPathExists(file);
    }
}
