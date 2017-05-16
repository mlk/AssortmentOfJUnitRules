package com.github.mlk.junit.matchers;

import com.github.mlk.junit.rules.HadoopDFSRule;
import org.apache.hadoop.fs.Path;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.io.IOException;

public class HadoopPathExists extends TypeSafeDiagnosingMatcher<HadoopDFSRule> {
    private final Path path;

    public HadoopPathExists(String path) {
        this(new Path(path));
    }

    public HadoopPathExists(Path path) {
        this.path = path;
    }

    @Override
    protected boolean matchesSafely(HadoopDFSRule item, Description mismatchDescription) {
        try {
            return item.exists(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void describeTo(Description description) {

    }
}
