# AssortmentOfJUnitRules

[![Build Status](https://travis-ci.org/mlk/AssortmentOfJUnitRules.svg)](https://travis-ci.org/mlk/AssortmentOfJUnitRules)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mlk/assortmentofjunitrules/badge.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.github.mlk%22%20AND%20a%3A%22assortmentofjunitrules%22)

This is a small selection of useful (for me) rules for building integration tests with AWS, Hadoop, Spark and Mongo.
These rules can be used together to start up a mini environment required for a test.  

The package has only one hard dependency - JUnit. All sub-rules will expect their requirements to be provided.

Each rule has a JUnit test which also serves as an example. 

## How to use

Utilizing your dependency framework of choice (Maven shown below) include both it and the rules dependencies
in your applications build system.

```
<dependency>
  <groupId>com.github.mlk</groupId>
  <artifactId>assortmentofjunitrules</artifactId>
  <version>1.5.**</version>
</dependency>
```
Check above for the most recent release.

## SparkRule

Starts up and shuts down a local Spark context between JUnit tests. Required [spark-core](http://mvnrepository.com/artifact/org.apache.spark).

Maven example:
```
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-core_2.11</artifactId>
            <version>1.4.1</version>
        </dependency>
```

[Example](https://github.com/mlk/AssortmentOfJUnitRules/blob/master/src/test/java/com/github/mlk/junit/rules/TestSparkRule.java)

## HadoopDFSRule 

Starts up and shuts down a local Hadoop DFS cluster between JUnit tests. Requires [hadoop-minicluster](http://mvnrepository.com/artifact/org.apache.hadoop/hadoop-minicluster).

Maven example:
```
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-client</artifactId>
            <version>2.8.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-minicluster</artifactId>
            <version>2.8.0</version>
        </dependency>
```

[Example](https://github.com/mlk/AssortmentOfJUnitRules/blob/master/src/test/java/com/github/mlk/junit/rules/TestHadoopRule.java)


## HadoopYarnRule 

Starts up and shuts down a local Hadoop DFS and YARN cluster between JUnit tests. Requires [hadoop-minicluster](http://mvnrepository.com/artifact/org.apache.hadoop/hadoop-minicluster).

[Example](https://github.com/mlk/AssortmentOfJUnitRules/blob/master/src/test/java/com/github/mlk/junit/rules/TestHadoopYarnRule.java)

## MongoRule

Uses [Flapdoodles' Embed Mongo](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo) to start up a Mongo
server before each test and shut it down after each test. Requires [de.flapdoodle.embed.mongo](http://mvnrepository.com/artifact/de.flapdoodle.embed/de.flapdoodle.embed.mongo)

Maven Example:
```
        <dependency>
            <groupId>de.flapdoodle.embed</groupId>
            <artifactId>de.flapdoodle.embed.mongo</artifactId>
            <version>2.0.0</version>
        </dependency>
```

[Example](https://github.com/mlk/AssortmentOfJUnitRules/blob/master/src/test/java/com/github/mlk/junit/rules/TestMongoRule.java)

## SftpRule

Uses [Apache Mina SSHd](https://mina.apache.org/sshd-project/) to start up a SFTP
server before each test and shut it down after each test. Requires [org.apache.sshd.sshd-core](https://mvnrepository.com/artifact/org.apache.sshd/sshd-core)

Maven Example:
```
        <dependency>
            <groupId>org.apache.sshd</groupId>
            <artifactId>sshd-core</artifactId>
            <version>1.4.0</version>
        </dependency>
```

[Example](https://github.com/mlk/AssortmentOfJUnitRules/blob/master/src/test/java/com/github/mlk/junit/rules/TestSftpRule.java)

## S3MockRule

Use [S3Mock](https://github.com/findify/s3mock) to create a S3 mock server at before each test and shut it down after each test. Requires [io.findify.s3mock_2.11](https://mvnrepository.com/artifact/io.findify/s3mock_2.11)

Maven Example:
```
      <dependency>
          <groupId>io.findify</groupId>
          <artifactId>s3mock_2.11</artifactId>
          <version>0.2.0</version>
      </dependency>
```

[Example](https://github.com/mlk/AssortmentOfJUnitRules/blob/master/src/test/java/com/github/mlk/junit/rules/S3MockRuleTest.java)

## LocalDynamoDbRule

Uses the [Local Amazon DynamoDB](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.html) to create a in-memory, local DynamoDB instance.
This rule handles all the SQLite native libraries stuff for you.

Maven Example:
```
<!--Dependency:-->
<dependencies>
    <dependency>
       <groupId>com.amazonaws</groupId>
       <artifactId>DynamoDBLocal</artifactId>
       <version>[1.11,2.0)</version>
    </dependency>
</dependencies>
<!--Custom repository:-->
<repositories>
    <repository>
       <id>dynamodb-local-oregon</id>
       <name>DynamoDB Local Release Repository</name>
       <url>https://s3-us-west-2.amazonaws.com/dynamodb-local/release</url>
    </repository>
</repositories>
```

[Example](https://github.com/mlk/AssortmentOfJUnitRules/blob/master/src/test/java/com/github/mlk/junit/rules/LocalDynamoDbRuleTest.java)

## HttpDynamoDbRule

Uses the [Local Amazon DynamoDB](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.html) to create a in-memory, local DynamoDB instance running over HTTP.
This rule handles all the SQLite native libraries stuff for you.

Maven Example:
```
<!--Dependency:-->
<dependencies>
    <dependency>
       <groupId>com.amazonaws</groupId>
       <artifactId>DynamoDBLocal</artifactId>
       <version>[1.11,2.0)</version>
    </dependency>
</dependencies>
<!--Custom repository:-->
<repositories>
    <repository>
       <id>dynamodb-local-oregon</id>
       <name>DynamoDB Local Release Repository</name>
       <url>https://s3-us-west-2.amazonaws.com/dynamodb-local/release</url>
    </repository>
</repositories>
```

[Example](https://github.com/mlk/AssortmentOfJUnitRules/blob/master/src/test/java/com/github/mlk/junit/rules/HttpDynamoDbRuleTest.java)
[Example of it in use](https://github.com/mlk/simples-migrations-dynamodb/tree/master/src/test/java/com/github/mlk/simples/migrations/dynamodb/example)

## PostgresRule

Uses [Embedded PostgreSQL Server](https://github.com/yandex-qatools/postgresql-embedded) to start up a Postgres server during each test.

Maven Example:
```
      <dependency>
          <groupId>ru.yandex.qatools.embed</groupId>
          <artifactId>postgresql-embedded</artifactId>
          <version>2.2</version>
      </dependency>
```

[Example](https://github.com/mlk/AssortmentOfJUnitRules/blob/master/src/test/java/com/github/mlk/junit/rules/PostgresRuleTest.java)


## ElasticSearchRule

Uses [Embedded ElasticSearch](https://github.com/allegro/embedded-elasticsearch) to start up a ElasticSearch server during each test.

Maven Example:
```
    <dependency>
        <groupId>pl.allegro.tech</groupId>
        <artifactId>embedded-elasticsearch</artifactId>
        <version>2.7.0</version>
        <scope>test</scope>
    </dependency>
```

[Example](https://github.com/mlk/AssortmentOfJUnitRules/blob/master/src/test/java/com/github/mlk/junit/rules/TestElasticSearcjRule.java)



## Helper Rules

### SpringDatabaseRule

Sets up all the Spring Boot stuff when using Spring Boot and a supported database.
This must be a `@ClassRule`.

### SpringElasticSearchRule

Sets up all the Spring Boot stuff when using Spring Boot and a Elastic Search.
This must be a `@ClassRule`.

### NativeLibraryRule

Finds native libraries in your classpath and moves them to a single folder. See the DynamoDB rules for an example.