package com.github.mlk.junit.rules;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.HdfsConfiguration;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.junit.rules.ExternalResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

public class HadoopDFSRule extends ExternalResource implements Serializable {
    private static final long serialVersionUID = 42L;

    private HdfsConfiguration conf = new HdfsConfiguration();
    private MiniDFSCluster cluster;

    private FileSystem mfs;
    private FileContext mfc;


    @Override
    protected void before() throws Throwable {
        cluster = new MiniDFSCluster.Builder(conf).numDataNodes(3).build();
        cluster.waitActive();
        mfs = cluster.getFileSystem();
        mfc = FileContext.getFileContext();
    };

    @Override
    protected void after() {
        cluster.shutdown(true);
    }

    public FileSystem getMfs() {
        return mfs;
    }

    public int getNameNodePort() {
        return cluster.getNameNodePort();
    }

    public void write(String filename, String content) throws IOException {
        FSDataOutputStream s = getMfs().create(new Path(filename));
        s.writeBytes(content);
        s.close();
    }

    public void copyResource(String filename, String resource) throws IOException {
        write(filename, IOUtils.toByteArray(getClass().getResource(resource)));
    }

    public void write(String filename, byte[] content) throws IOException {
        FSDataOutputStream s = getMfs().create(new Path(filename));
        s.write(content);
        s.close();
    }

    public String read(String filename) throws IOException {
        Path p = new Path(filename);
        int size = (int)getMfs().getFileStatus(p).getLen();
        FSDataInputStream is = getMfs().open(p);
        byte[] content = new byte[size];
        is.readFully(content);
        return new String(content);
    }

    public void printout(String filename) throws IOException {
        Path p = new Path(filename);
        int size = (int)getMfs().getFileStatus(p).getLen();
        FSDataInputStream is = getMfs().open(p);
        byte[] content = new byte[size];
        is.readFully(content);
        System.out.println(new String(content));
    }

    public boolean exist(String filename) throws IOException {
        try {
            return getMfs().listFiles(new Path(filename), true).hasNext();
        } catch (FileNotFoundException e) {
            return false;
        }
    }
}

