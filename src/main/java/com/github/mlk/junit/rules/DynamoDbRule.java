package com.github.mlk.junit.rules;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

public interface DynamoDbRule {
  AmazonDynamoDB getClient();
}
