package com.github.mlk.junit.rules;

import lombok.AllArgsConstructor;
import org.junit.rules.ExternalResource;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import java.util.UUID;

@AllArgsConstructor
public class ElasticSearchRule extends ExternalResource {
    private final EmbeddedElastic.Builder builder;

    private EmbeddedElastic embeddedElastic;

    public ElasticSearchRule(EmbeddedElastic.Builder builder) {
        this.builder = builder;
    }


    public ElasticSearchRule(String version, int httpPort, int transportPort) {
        this(EmbeddedElastic.builder()
                .withElasticVersion(version)
                .withSetting(PopularProperties.CLUSTER_NAME, UUID.randomUUID().toString())
                .withSetting(PopularProperties.HTTP_PORT, httpPort)
                .withSetting(PopularProperties.TRANSPORT_TCP_PORT, transportPort));
    }


    public ElasticSearchRule() {
        this("6.0.0", Helper.selectFreePort(), Helper.selectFreePort());
    }

    @Override
    protected void before() throws Throwable {
        embeddedElastic = builder.build();

        embeddedElastic.start();
    }

    @Override
    protected void after() {
        embeddedElastic.stop();
    }

    public EmbeddedElastic getEmbeddedElastic() {
        return embeddedElastic;
    }
}
