package com.github.mlk.junit.rules;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.junit.Rule;
import org.junit.Test;

public class PostgresRuleTest {

  @Rule
  public PostgresRule subject = new PostgresRule();

  @Test
  public void connectToPostgressUsingJdbc() throws Exception {
    Class.forName("org.postgresql.Driver");
    try (Connection connection = DriverManager.getConnection(subject.getDatasourceUrl(), subject.getUsername(),
        subject.getPassword())) {
      try (Statement statement = connection.createStatement()) {
        statement.execute("select * from information_schema.tables");
        try (ResultSet rs = statement.getResultSet()) {

          assertThat(rs.getMetaData().getTableName(1), is("tables"));
        }
      }
    }
  }
}