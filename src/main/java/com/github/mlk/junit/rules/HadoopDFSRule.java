package com.github.mlk.junit.rules;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.HdfsConfiguration;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.junit.rules.ExternalResource;

import java.io.FileNotFoundException;
import java.io.IOException;

/** Starts and stops a local Hadoop DFS cluster for integration testing against Hadoop.
 * For an example of it in action see the <a href="http://tinyurl.com/hadooprule">tests for this class.</a>
 */
public class HadoopDFSRule extends ExternalResource {
    private HdfsConfiguration conf;
    private MiniDFSCluster cluster;

    private FileSystem mfs;
    private FileContext mfc;

    protected HdfsConfiguration createConfiguration() {
        return new HdfsConfiguration();
    }

    public HdfsConfiguration getConfiguration() {
        return conf;
    }

    @Override
    protected void before() throws Throwable {
        conf = createConfiguration();
        cluster = new MiniDFSCluster.Builder(conf).numDataNodes(3).build();
        cluster.waitActive();
        mfs = cluster.getFileSystem();
        mfc = FileContext.getFileContext();
    }

    @Override
    protected void after() {
        cluster.shutdown(true);
    }

    /** @return Returns the file system on the Hadoop mini-cluster. */
    public FileSystem getMfs() {
        return mfs;
    }

    /** @return Returns the port of the name node in the mini-cluster. */
    public int getNameNodePort() {
        return cluster.getNameNodePort();
    }

    /** Writes the following content to the Hadoop cluster.
     *
     * @param filename    File to be created
     * @param content     The content to write
     * @throws IOException Anything.
     */
    public void write(String filename, String content) throws IOException {
        FSDataOutputStream s = getMfs().create(new Path(filename));
        s.writeBytes(content);
        s.close();
    }

    /** Copies the content of the given resource into Hadoop.
     *
     * @param filename     File to be created
     * @param resource     Resource to copy
     * @throws IOException Anything
     */
    public void copyResource(String filename, String resource) throws IOException {
        write(filename, IOUtils.toByteArray(getClass().getResource(resource)));
    }

    /** Writes the following content to the Hadoop cluster.
     *
     * @param filename    File to be created
     * @param content     The content to write
     * @throws IOException Anything
     */
    public void write(String filename, byte[] content) throws IOException {
        FSDataOutputStream s = getMfs().create(new Path(filename));
        s.write(content);
        s.close();
    }

    /** Fully reads the content of the given file.
     *
     * @param filename    File to read
     * @return            Content of the file
     * @throws IOException Anything
     */
    public String read(String filename) throws IOException {
        Path p = new Path(filename);
        int size = (int)getMfs().getFileStatus(p).getLen();
        FSDataInputStream is = getMfs().open(p);
        byte[] content = new byte[size];
        is.readFully(content);
        return new String(content);
    }

    /** Checks if the given file exists on Hadoop.
     *
     * @param filename Filename to check
     * @return true if the file exists
     * @throws IOException Anything
     */
    public boolean exist(String filename) throws IOException {
        try {
            return getMfs().listFiles(new Path(filename), true).hasNext();
        } catch (FileNotFoundException e) {
            return false;
        }
    }
}

