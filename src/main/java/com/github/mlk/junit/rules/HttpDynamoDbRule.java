package com.github.mlk.junit.rules;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import java.util.regex.Pattern;
import org.junit.rules.ExternalResource;

/** Runs a DynamoDB server exposed over HTTP.
 * It handles all the SQLite native libraries stuff for you.
 *
 * Heavily inspired by the answers here: https://stackoverflow.com/questions/26901613/easier-dynamodb-local-testing
 */
public class HttpDynamoDbRule extends ExternalResource implements DynamoDbRule{
  private NativeLibraryRule nativeLibraryRule;
  private DynamoDBProxyServer server;
  private final int defaultPort;
  private final boolean useFakeCreds;
  private int port;

  public HttpDynamoDbRule() {
    this(-1, true);
  }

  public HttpDynamoDbRule(int defaultPort, boolean useFakeCreds) {
    this.defaultPort = defaultPort;
    this.useFakeCreds = useFakeCreds;
    nativeLibraryRule = new NativeLibraryRule(Pattern.compile(".*sqlite.*"));
  }

  public AmazonDynamoDB getClient() {
    return AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
        new AwsClientBuilder.EndpointConfiguration(getEndpoint(), "us-west-2"))
        .build();
  }

  @Override
  protected void before() throws Throwable {
    nativeLibraryRule.before();
    System.setProperty("sqlite4java.library.path", nativeLibraryRule.getNativeLibrariesFolder().toString());

    if (useFakeCreds) {
      System.setProperty("aws.accessKeyId", "x");
      System.setProperty("aws.secretKey", "x");
    }

    if(defaultPort > -1) {
      port = defaultPort;
    } else {
      port = Helper.findRandomOpenPortOnAllLocalInterfaces();
    }


    final String[] localArgs = { "-inMemory", "-port", Integer.toString(port) };
    server = ServerRunner.createServerFromCommandLineArgs(localArgs);
    server.start();
  }

  @Override
  protected void after() {
    try {
      server.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }

    nativeLibraryRule.after();
  }

  public int getPort() {
    return port;
  }

  public String getEndpoint() {
    return "http://localhost:" + getPort();
  }
}
