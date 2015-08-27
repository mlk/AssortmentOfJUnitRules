package com.github.mlk.junit.matchers;

import com.github.mlk.junit.rules.HadoopDFSRule;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.io.IOException;

public class HadoopFileExists extends TypeSafeDiagnosingMatcher<HadoopDFSRule> {
    private final String path;

    public HadoopFileExists(String path) {
        this.path = path;
    }


    @Override
    protected boolean matchesSafely(HadoopDFSRule item, Description mismatchDescription) {
        try {
            return item.exist(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void describeTo(Description description) {

    }
}
