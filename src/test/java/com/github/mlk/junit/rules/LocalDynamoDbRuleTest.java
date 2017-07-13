package com.github.mlk.junit.rules;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.github.mlk.junit.rules.helpers.dynamodb.DynamoExample;
import java.util.UUID;
import org.junit.Rule;
import org.junit.Test;

public class LocalDynamoDbRuleTest {
  @Rule
  public LocalDynamoDbRule subject = new LocalDynamoDbRule();

  @Test
  public void getterSetterTest() {
    String randomValue = UUID.randomUUID().toString();

    DynamoExample exampleClient = new DynamoExample(subject.getClient());
    exampleClient.createTable();
    exampleClient.setValue(1L, randomValue);

    assertThat(exampleClient.getValue(1L), is(randomValue));
  }

}