package com.github.mlk.junit.rules;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import java.util.regex.Pattern;
import org.junit.rules.ExternalResource;

/** Runs a DynamoDB server exposed over HTTP.
 * It handles all the SQLite native libraries stuff for you.
 */
public class HttpDynamoDbRule extends ExternalResource {
  private NativeLibraryRule nativeLibraryRule;
  private DynamoDBProxyServer server;
  private final int defaultPort;
  private int port;

  public HttpDynamoDbRule() {
    this(-1);
  }

  public HttpDynamoDbRule(int defaultPort) {
    this.defaultPort = defaultPort;
    nativeLibraryRule = new NativeLibraryRule(Pattern.compile(".*sqlite.*"));
  }

  @Override
  protected void before() throws Throwable {
    nativeLibraryRule.before();
    System.setProperty("sqlite4java.library.path", nativeLibraryRule.getNativeLibrariesFolder().toString());

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
