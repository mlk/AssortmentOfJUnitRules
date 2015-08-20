package com.github.mlk.junit.rules;

import org.apache.hadoop.hdfs.HdfsConfiguration;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.server.MiniYARNCluster;

/** Starts and stops a local Hadoop DFS & YARN cluster for integration testing against Hadoop.
 * For an example of it in action see the <a href="http://tinyurl.com/yarnrule">tests for this class.</a>
 */
public class HadoopYarnRule extends HadoopDFSRule {
    private MiniYARNCluster yarnCluster;
    private final String name;

    /** Creates a Yarn rule with the following test name.
     *
     * @param name The test name
     */
    public HadoopYarnRule(String name) {
        this.name = name;
    }

    protected HdfsConfiguration createConfiguration() {
        return new HdfsConfiguration(new YarnConfiguration());
    }

    @Override
    protected void before() throws Throwable {
        super.before();

        yarnCluster = new MiniYARNCluster(name, 1, 1, 1);
        yarnCluster.init(getConfiguration());
        yarnCluster.start();
    }

    @Override
    protected void after() {
        super.after();
        yarnCluster.stop();
    }
}
