package com.github.mlk.junit.rules.helpers.hadoop;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WordCounterMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

  private final static IntWritable countOne = new IntWritable(1);
  private final Text reusableText = new Text();

  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    String[] data = value.toString().split("\\s");
    for (String word : data) {
      reusableText.set(word);
      context.write(reusableText, countOne);
    }
  }
}

