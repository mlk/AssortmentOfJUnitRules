package com.github.mlk.junit.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.waiters.AmazonDynamoDBWaiters;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

/** This is a helper class for the LocalDynamoDbRule. Alas in the 1.11.0.1 release of the Local
 * version of the DynamoDB client throws a UnsupportedException on 'waiters'. This provides a
 * wrapper which provides a waiter.
 */
@AllArgsConstructor
public class DynamoDBClientWithStubbedWaiter implements AmazonDynamoDB {
  @Delegate(types = AmazonDynamoDB.class, excludes = Exclude.class)
  private AmazonDynamoDB wrapped;

  @Override
  public AmazonDynamoDBWaiters waiters() {
    return new AmazonDynamoDBWaiters(wrapped);
  }

  interface Exclude {
    AmazonDynamoDBWaiters waiters();
  }
}
