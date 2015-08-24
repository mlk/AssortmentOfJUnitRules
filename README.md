# AssortmentOfJUnitRules

This is a small selection of useful (for me) rules for building integration tests with Hadoop, Spark and Mongo. 
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
  <version>1.2.1</version>
</dependency>
```

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
            <version>2.6.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-minicluster</artifactId>
            <version>2.6.0</version>
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
            <version>1.28</version>
        </dependency>
```

[Example](https://github.com/mlk/AssortmentOfJUnitRules/blob/master/src/test/java/com/github/mlk/junit/rules/TestMongoRule.java)

## VagrantRule

Very simplistic implementation of Vagrant rule. Just starts up and shutdowns a Vagrant instance. This needs Vagrant to be installed locally.