package com.github.mlk.junit.rules;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import java.util.regex.Pattern;
import org.junit.rules.ExternalResource;

/** This rule creates a LocalDynamoDB instance.
 * It handles all the SQLite native libraries stuff for you.
 *
 * Heavily inspired by the answers here: https://stackoverflow.com/questions/26901613/easier-dynamodb-local-testing
 */
public class LocalDynamoDbRule extends ExternalResource implements DynamoDbRule {

  private AmazonDynamoDB client;
  private NativeLibraryRule nativeLibraryRule;

  public AmazonDynamoDB getClient() {
    return client;
  }

  public LocalDynamoDbRule() {
    nativeLibraryRule = new NativeLibraryRule(Pattern.compile(".*sqlite.*"));
  }

  @Override
  protected void before() throws Throwable {
    nativeLibraryRule.before();
    System.setProperty("sqlite4java.library.path", nativeLibraryRule.getNativeLibrariesFolder().toString());

    client = DynamoDBEmbedded.create().amazonDynamoDB();
  }

  @Override
  protected void after() {
    try {
      client.shutdown();
    } catch (Exception e) {
      e.printStackTrace();
    }
    nativeLibraryRule.after();
  }
}
