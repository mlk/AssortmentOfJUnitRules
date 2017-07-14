package com.github.mlk.junit.rules;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.github.mlk.junit.rules.helpers.dynamodb.DynamoExample;
import java.util.UUID;
import org.junit.Rule;
import org.junit.Test;

public class HttpDynamoDbRuleTest {
  @Rule
  public HttpDynamoDbRule subject = new HttpDynamoDbRule();

  @Test
  public void getterSetterTest() {
    String randomValue = UUID.randomUUID().toString();

    DynamoExample exampleClient = new DynamoExample(AmazonDynamoDBClientBuilder
        .standard()
        .withEndpointConfiguration(new EndpointConfiguration(subject.getEndpoint(), "eu-west-1"))
        .build());
    exampleClient.createTable();
    exampleClient.setValue(1L, randomValue);

    assertThat(exampleClient.getValue(1L), is(randomValue));
  }
}