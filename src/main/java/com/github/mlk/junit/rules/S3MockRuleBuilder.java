package com.github.mlk.junit.rules;

import com.github.mlk.junit.rules.S3MockRule.FakeFile;
import java.util.LinkedHashSet;
import java.util.Set;

public class S3MockRuleBuilder {
  private final Set<String> buckets = new LinkedHashSet<>();
  private final Set<FakeFile> files = new LinkedHashSet<>();

  public S3MockRuleBuilder bucket(String bucketName) {
    buckets.add(bucketName);
    return this;
  }

  public S3MockRule build() {
    return new S3MockRule(buckets, files);
  }

  public S3MockRuleBuilder file(String bucketName, String fileName, String content) {
    files.add(new FakeFile(bucketName, fileName, content));
    buckets.add(bucketName);
    return this;
  }

}
