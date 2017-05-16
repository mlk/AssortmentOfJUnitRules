package com.github.mlk.junit.rules.helpers.hadoop;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class NoAccessToHadoopRule {

  private final String hadoopUrl;

  public NoAccessToHadoopRule(String hadoopUrl) {
    this.hadoopUrl = hadoopUrl;
  }


  public List<String> content() throws IOException {
    Configuration confHadoop = new Configuration();
    confHadoop.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
    confHadoop.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
    try (FileSystem hdfs = FileSystem.get(URI.create(hadoopUrl), confHadoop)) {
      return IOUtils.readLines(hdfs.open(new Path("/new_file.txt")));
    }
  }
}
