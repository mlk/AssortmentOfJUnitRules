package com.github.mlk.junit.rules;

import static ru.yandex.qatools.embed.postgresql.distribution.Version.Main.V9_4;

import de.flapdoodle.embed.process.distribution.IVersion;
import org.junit.rules.ExternalResource;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.PostgresStarter;
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig.Credentials;
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig.Net;
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig.Storage;
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig.Timeout;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;

public class PostgresRule extends ExternalResource implements DatabaseRule {
  private PostgresProcess postgresRuntime;
  private final int defaultPort;
  private final String databaseName;
  private final String username;
  private final String password;
  private final IVersion version;
  private int port;

  public PostgresRule(int defaultPort, String databaseName, String username, String password, IVersion version) {
    this.defaultPort = defaultPort;
    this.databaseName = databaseName;
    this.username = username;
    this.password = password;
    this.version = version;
  }

  public PostgresRule() {
    this(-1, "postgres", "postgres", "postgres", V9_4);
  }

  @Override
  public String getDatasourceUrl() {
    return "jdbc:postgresql://localhost:" + port + "/" + databaseName;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void before() throws Throwable {
    port = defaultPort;
    if (port  < 0) {
      port = Helper.findRandomOpenPortOnAllLocalInterfaces();
    }

    postgresRuntime = PostgresStarter.getDefaultInstance()
        .prepare(
            new PostgresConfig(
                version,
                new Net("localhost", port),
                new Storage(databaseName),
                new Timeout(30000),
                new Credentials(username, password)))
        .start();
  }

  @Override
  public void after() {
    postgresRuntime.stop();
  }
}
