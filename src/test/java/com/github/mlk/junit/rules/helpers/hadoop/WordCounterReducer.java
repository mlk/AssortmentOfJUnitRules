package com.github.mlk.junit.rules.helpers.hadoop;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class WordCounterReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    private IntWritable outValue = new IntWritable();
    @Override
    protected void reduce(Text token, Iterable<IntWritable> counts, Context context)
            throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable count : counts) {
            sum += count.get();
        }
        outValue.set(sum);
        context.write(token, outValue);
    }
}
