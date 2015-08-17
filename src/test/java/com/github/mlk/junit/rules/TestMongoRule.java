package com.github.mlk.junit.rules;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TestMongoRule {
    @Rule
    public MongoRule subject = new MongoRule(12345);

    @Test
    public void helloWorld() {
        MongoClient mongoClient = new MongoClient("localhost", 12345);
        MongoDatabase db = mongoClient.getDatabase("TEST_DATABASE");
        db.createCollection("TEST_COLLECTION");
        db.getCollection("TEST_COLLECTION").insertOne(new Document());


        assertThat(db.getCollection("TEST_COLLECTION").count(), is(1L));
    }
}
