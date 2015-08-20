package com.github.mlk.junit.rules;

import org.apache.hadoop.hdfs.HdfsConfiguration;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.server.MiniYARNCluster;

public class HadoopYarnRule extends HadoopDFSRule {
    private MiniYARNCluster yarnCluster;
    private final String name;

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
