package com.github.mlk.junit.rules;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
import de.flapdoodle.embed.mongo.distribution.Version.Main;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.rules.ExternalResource;

/**
 * Utilizing flapdoodle.embed.mongo, downloads Mongo and runs an instance per test. For an example
 * of it in action see the <a href="httphttp://tinyurl.com/mongorule">tests for this class.</a>
 */
public class MongoRule extends ExternalResource {

  private MongodExecutable mongodExe;
  private MongodProcess mongod;
  private final int defaultPort;
  private int currentPort;
  private final IFeatureAwareVersion version;

  /**
   * @param port The port to run Mongo DB on. Recommendation: Don't use a standard Mongo port. -1 will use a random port.
   */
  public MongoRule(int port, IFeatureAwareVersion version) {
    this.defaultPort = port;
    this.version = version;
  }

  public MongoRule() {
    this(-1, Main.V2_3);
  }

  @Override
  public void before() throws Exception {
    if(defaultPort < 0) {
      currentPort = Helper.findRandomOpenPortOnAllLocalInterfaces();
    } else {
      currentPort = defaultPort;
    }

    IMongodConfig mongodConfig = new MongodConfigBuilder()
        .version(version)
        .net(new Net("localhost", currentPort, Network.localhostIsIPv6()))
        .build();

    MongodStarter runtime = MongodStarter.getDefaultInstance();
    mongodExe = runtime.prepare(mongodConfig);
    mongod = mongodExe.start();
  }

  @Override
  public void after() {
    if (this.mongod != null) {
      this.mongod.stop();
      this.mongodExe.stop();
    }
  }

  public int getPort() {
    return currentPort;
  }
}
