package com.github.mlk.junit.rules;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Rule;
import org.junit.Test;

public class S3MockRuleTest {
  @Rule
  public S3MockRule subject = new S3MockRuleBuilder()
      .bucket("bucket-name")
      .file("bucket-name", "fileName", "content")
      .file("bucket-name", "fileName2", "content")
      .file("new-bucket", "fileName", "content")
      .build();

  @Test
  public void createsBuckets() {
    List<String> names = subject.getAmazonS3Client().listBuckets().stream().map(Bucket::getName).sorted().collect(Collectors.toList());
    assertThat(names, is(Arrays.asList("bucket-name", "new-bucket")));
  }

  @Test
  public void createsFile() {
    List<String> names = newArrayList(
        S3Objects.inBucket(subject.getAmazonS3Client(), "bucket-name").iterator())
        .stream().map(
        S3ObjectSummary::getKey).sorted().collect(Collectors.toList());

    assertThat(names, is(Arrays.asList("fileName", "fileName2")));
  }
}