package com.github.mlk.junit.rules;

import org.junit.rules.ExternalResource;

/** This is a helper for Spring Boot applications
 * It sets up the spring.datasource props for the database in question.
 *
 */
public class SpringDatabaseRule extends ExternalResource {
  private final DatabaseRule databaseRule;

  public SpringDatabaseRule(DatabaseRule databaseRule) {
    this.databaseRule = databaseRule;
  }

  @Override
  protected void before() throws Throwable {
    databaseRule.before();
    System.setProperty("spring.datasource.url", databaseRule.getDatasourceUrl());
    System.setProperty("spring.datasource.username", databaseRule.getUsername());
    System.setProperty("spring.datasource.password", databaseRule.getPassword());
  }

  @Override
  protected void after() {
    databaseRule.after();
  }
}
