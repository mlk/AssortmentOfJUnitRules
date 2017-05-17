package com.github.mlk.junit.rules;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.rules.ExternalResource;

/**
 * Utilizing flapdoodle.embed.mongo, downloads Mongo and runs an instance per test. For an example
 * of it in action see the <a href="httphttp://tinyurl.com/mongorule">tests for this class.</a>
 *
 * TODO: - Use the Random Port thingie.
 */
public class MongoRule extends ExternalResource {

  private MongodExecutable mongodExe;
  private MongodProcess mongod;
  private final int defaultPort;
  private int currentPort;

  /**
   * @param port The port to run Mongo DB on. Recommendation: Don't use a standard Mongo port. -1 will use a random port.
   */
  public MongoRule(int port) {
    this.defaultPort = port;
  }

  public MongoRule() {
    defaultPort = -1;
  }

  @Override
  public void before() throws Exception {
    if(defaultPort < 0) {
      currentPort = Helper.findRandomOpenPortOnAllLocalInterfaces();
    } else {
      currentPort = defaultPort;
    }

    MongodStarter runtime = MongodStarter.getDefaultInstance();
    mongodExe = runtime.prepare(new MongodConfig(Version.V2_3_0, currentPort, Network.localhostIsIPv6()));
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
