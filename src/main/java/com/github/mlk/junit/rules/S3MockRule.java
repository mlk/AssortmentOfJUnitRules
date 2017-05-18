package com.github.mlk.junit.rules;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.junit.rules.ExternalResource;

public class S3MockRule extends ExternalResource {
  private S3Mock s3Mock;
  private InetSocketAddress localAddress;
  private AmazonS3 amazonS3;
  private final Set<String> buckets;
  private final Set<FakeFile> files;

  public S3MockRule() {
    this(Collections.emptySet(), Collections.emptySet());
  }

  public S3MockRule(Set<String> buckets, Set<FakeFile> files) {
    this.buckets = buckets;
    this.files = files;
  }

  protected void before() throws Throwable {
    s3Mock = S3Mock.create(Helper.findRandomOpenPortOnAllLocalInterfaces());
    localAddress = s3Mock.start().localAddress();
    amazonS3 = AmazonS3ClientBuilder.standard()
        .withEndpointConfiguration(new EndpointConfiguration(getEndpoint(), "eu-west-1"))
        .build();

    buckets.forEach(amazonS3::createBucket);
    files.forEach(fakeFile -> amazonS3.putObject(fakeFile.bucket, fakeFile.name, fakeFile.content));
  }

  protected void after() {
    if(localAddress != null) {
      s3Mock.stop();
    }
  }

  public String getEndpoint() {
    return "http://127.0.0.1:" + localAddress.getPort();
  }

  public AmazonS3 getAmazonS3Client() {
    return amazonS3;
  }

  @Value
  @EqualsAndHashCode(exclude="content")
  static class FakeFile {
    String bucket;
    String name;
    String content;
  }
}
