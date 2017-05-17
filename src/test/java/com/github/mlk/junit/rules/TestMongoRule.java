package com.github.mlk.junit.rules;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Rule;
import org.junit.Test;

public class TestMongoRule {

  @Rule
  public MongoRule subject = new MongoRule();

  /**
   * "Hello world" example that creates a collection, inserts an item and verifies the item exists.
   */
  @Test
  public void helloWorld() {
    MongoClient mongoClient = new MongoClient("localhost", subject.getPort());
    MongoDatabase db = mongoClient.getDatabase("TEST_DATABASE");
    db.createCollection("TEST_COLLECTION");
    db.getCollection("TEST_COLLECTION").insertOne(new Document());

    assertThat(db.getCollection("TEST_COLLECTION").count(), is(1L));
  }
}
