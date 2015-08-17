package com.github.mlk.junit.rules;

import com.github.mlk.junit.rules.helpers.hadoop.NoAccessToHadoopRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestHadoopRule {

    @Rule
    public HadoopDFSRule subject = new HadoopDFSRule();

    @Test
    public void helloWorld() throws IOException {
        subject.copyResource("/new_file.txt", "/testfile.txt");

        List<String> content = new NoAccessToHadoopRule("hdfs://localhost:" + subject.getNameNodePort()).content();

        assertThat(content.get(0), is("one"));
        assertThat(content.get(1), is("two two"));
        assertThat(content.get(2), is("three three three"));
    }


    @Test
    public void whenFileDoesNotExistThenReturnFalse() throws IOException {
        assertThat(subject.exist("/hello.txt"), is(false));
    }

    @Test
    public void whenFileExistsThenReturnTrue() throws IOException {
        subject.copyResource("/hello.txt", "/testfile.txt");

        assertThat(subject.exist("/hello.txt"), is(true));
    }

    @Test
    public void testRead() throws IOException {
        subject.write("/new_file.txt", "the_content");

        String content = subject.read("/new_file.txt");

        assertThat(content, is("the_content"));
    }
}

