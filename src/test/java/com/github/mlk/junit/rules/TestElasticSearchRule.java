package com.github.mlk.junit.rules;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class TestElasticSearchRule {

    @ClassRule
    public static ElasticSearchRule subject = new ElasticSearchRule();


    @Test
    public void ping() throws IOException {
        try (RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", subject.getEmbeddedElastic().getHttpPort(), "http")))) {

            assertTrue(client.ping());
        }
    }
}
