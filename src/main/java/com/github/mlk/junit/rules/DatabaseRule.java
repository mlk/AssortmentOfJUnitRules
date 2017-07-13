package com.github.mlk.junit.rules;

public interface DatabaseRule {
  String getDatasourceUrl();

  String getUsername();

  String getPassword();

  void before() throws Throwable;

  void after();
}
