package com.github.mlk.junit.rules.helpers.dynamodb;

import static java.util.Collections.singleton;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.github.mlk.junit.dynamo.DynamoDBClientWithStubbedWaiter;
import com.github.mlk.junit.rules.LocalDynamoDbRule;
import java.util.ArrayList;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;

public class DynamoDBClientWithStubbedWaiterTest {

  @Rule
  public LocalDynamoDbRule dynamoDbRule = new LocalDynamoDbRule();

  @Test
  public void waiterMethodsShouldWorkWithWrapper() throws InterruptedException {
    DynamoDBClientWithStubbedWaiter subject = new DynamoDBClientWithStubbedWaiter(
        dynamoDbRule.getClient());

    DynamoDB db = new DynamoDB(subject);

    AttributeDefinition id = new AttributeDefinition("id", ScalarAttributeType.S);
    List<KeySchemaElement> keySchema = new ArrayList<>();

    keySchema.add(
        new KeySchemaElement()
            .withAttributeName(id.getAttributeName())
            .withKeyType(KeyType.HASH)
    );

    ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput();
    provisionedThroughput.setReadCapacityUnits(10L);
    provisionedThroughput.setWriteCapacityUnits(10L);

    CreateTableRequest request = new CreateTableRequest()
        .withTableName("test_table")
        .withKeySchema(keySchema)
        .withAttributeDefinitions(singleton(id))
        .withProvisionedThroughput(provisionedThroughput);

    Table result = db.createTable(request);
    TableDescription description = result.waitForActive();

    assertThat(description.getTableName(), is("test_table"));
  }
}
