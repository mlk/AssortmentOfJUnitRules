package com.github.mlk.junit.matchers;

import com.github.mlk.junit.rules.HadoopDFSRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static com.github.mlk.junit.matchers.HadoopMatchers.exists;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class HadoopMatchersTest {
    @Rule
    public HadoopDFSRule hadoop = new HadoopDFSRule();

    @Test
    public void whenFileExistsReturnTrue() throws IOException {
        hadoop.write("/hello.txt", "content");

        assertThat(hadoop, exists("/hello.txt"));
    }

    @Test
    public void whenFileDoesNotExistThenReturnFalse() throws IOException {
        assertThat(hadoop, not(exists("/hello.txt")));
    }
}