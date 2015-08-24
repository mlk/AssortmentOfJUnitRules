package com.github.mlk.junit.rules;

import org.junit.rules.ExternalResource;

import java.io.File;
import java.io.IOException;

/** A super-simple Vagrant run example. This rule starts up a vagrant box at the start of the test and shuts it down at
 * the end.
 *
 */
public class VagrantRule extends ExternalResource {
    private final File workingDirectory;
    private final String box;

    public VagrantRule(File workingDirectory, String box) {
        if (!(workingDirectory.exists() && workingDirectory.isDirectory())) {
           throw new IllegalStateException(workingDirectory + " does not exist or not a folder");
        }
        this.workingDirectory = workingDirectory;
        if(box == null) {
            this.box = "default";
        } else {
            this.box = box;
        }
    }

    public VagrantRule(File workingDirectory) {
        this(workingDirectory, null);
    }


    @Override
    protected void before() throws Throwable {
        ProcessBuilder pb = new ProcessBuilder("vagrant", "up", box)
                .inheritIO()
                .directory(workingDirectory);
        Process p = pb.start();
        p.waitFor();
        if(p.exitValue() != 0) {
            throw new IOException("Unable to start vagrant box");
        }
    }

    @Override
    protected void after() {
        try {
            ProcessBuilder pb = new ProcessBuilder("vagrant", "halt", box)
                    .inheritIO()
                    .directory(workingDirectory);
            Process p = pb.start();
            p.waitFor();
            p.exitValue();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

}
