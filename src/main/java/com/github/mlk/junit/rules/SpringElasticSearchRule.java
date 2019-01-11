package com.github.mlk.junit.rules;

import org.junit.rules.ExternalResource;

public class SpringElasticSearchRule extends ExternalResource {
    private final ElasticSearchRule databaseRule;
    private String oldElasticSearchPort;
    private String oldElasticSearchHost;

    public SpringElasticSearchRule(ElasticSearchRule databaseRule) {
        this.databaseRule = databaseRule;
    }

    @Override
    protected void before() throws Throwable {
        databaseRule.before();
        oldElasticSearchHost = System.getProperty("elasticsearch.host");
        oldElasticSearchPort = System.getProperty("elasticsearch.port");

        System.setProperty("elasticsearch.host", "localhost");
        System.setProperty("elasticsearch.port", "" + databaseRule.getEmbeddedElastic().getHttpPort());
    }

    @Override
    protected void after() {
        databaseRule.after();
        System.clearProperty("elasticsearch.port");
        System.clearProperty("elasticsearch.host");

        if(oldElasticSearchHost != null) {
            System.setProperty("elasticsearch.host", oldElasticSearchHost);
        }

        if(oldElasticSearchPort != null) {
            System.setProperty("elasticsearch.port", oldElasticSearchPort);
        }
    }


    public static SpringElasticSearchRule defaultElasticSearch() {
        return new SpringElasticSearchRule(new ElasticSearchRule());
    }
}

