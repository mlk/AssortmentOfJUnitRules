package com.github.mlk.junit.rules.helpers.dynamodb;

import static java.util.Collections.singleton;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamoExample {
  private final AmazonDynamoDB client;

  private final AttributeDefinition sequenceNumber = new AttributeDefinition("sequence_number", ScalarAttributeType.N);
  private final AttributeDefinition valueAttribute = new AttributeDefinition("value", ScalarAttributeType.S);

  public DynamoExample(AmazonDynamoDB client) {
    this.client = client;
  }

  public void createTable() {
    List<KeySchemaElement> keySchema = new ArrayList<>();

    keySchema.add(
        new KeySchemaElement()
            .withAttributeName(sequenceNumber.getAttributeName())
            .withKeyType(KeyType.HASH)
    );

    ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput();
    provisionedThroughput.setReadCapacityUnits(10L);
    provisionedThroughput.setWriteCapacityUnits(10L);

    CreateTableRequest request = new CreateTableRequest()
        .withTableName("example_table")
        .withKeySchema(keySchema)
        .withAttributeDefinitions(singleton(sequenceNumber))
        .withProvisionedThroughput(provisionedThroughput);

    client.createTable(request);
  }

  public void setValue(long key, String value) {
    Map<String, AttributeValue> firstId = new HashMap<>();
    firstId.put(sequenceNumber.getAttributeName(), new AttributeValue().withN(Long.toString(key)));
    firstId.put(valueAttribute.getAttributeName(), new AttributeValue().withS(value));


    client.putItem(new PutItemRequest()
        .withTableName("example_table")
        .withItem(firstId));
  }

  public String getValue(long key) {
    Map<String, AttributeValue> result = client.getItem(new GetItemRequest()
        .withTableName("example_table")
        .withKey(Collections.singletonMap(sequenceNumber.getAttributeName(), new AttributeValue().withN(Long.toString(key))))).getItem();

    return result.get(valueAttribute.getAttributeName()).getS();
  }
}
