package com.github.mlk.junit.rules;

import java.io.IOException;
import java.net.ServerSocket;

public class Helper {

  private Helper() {
  }

  public static int findRandomOpenPortOnAllLocalInterfaces() throws IOException {
    try (ServerSocket socket = new ServerSocket(0)) {
      return socket.getLocalPort();
    }
  }
}
