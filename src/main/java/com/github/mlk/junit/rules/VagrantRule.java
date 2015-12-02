package com.github.mlk.junit.rules;

import org.junit.rules.ExternalResource;

import java.io.File;
import java.io.IOException;

/** A super-simple Vagrant run example. This rule starts up a vagrant box at the start of the test and shuts it down at
 * the end.
 * You may optionally leave the vagrant box running after the test. This will make runs fast, but at the expense of
 * of having state float about after the tests have completed.
 */
public class VagrantRule extends ExternalResource {
    private final File workingDirectory;
    private final String box;
    private final boolean shutdown;

    public VagrantRule(File workingDirectory, String box, boolean shutdown) {
        if (!(workingDirectory.exists() && workingDirectory.isDirectory())) {
           throw new IllegalStateException(workingDirectory + " does not exist or not a folder");
        }
        this.workingDirectory = workingDirectory;
        if(box == null) {
            this.box = "default";
        } else {
            this.box = box;
        }
        this.shutdown = shutdown;
    }

    public VagrantRule(File workingDirectory) {
        this(workingDirectory, null, true);
    }


    @Override
    protected void before() throws Throwable {
        up();
    }

    public void up() throws InterruptedException, IOException {
        ProcessBuilder pb = new ProcessBuilder("vagrant", "up", box)
                .inheritIO()
                .directory(workingDirectory);
        Process p = pb.start();
        p.waitFor();
        if(p.exitValue() != 0) {
            throw new IOException("Unable to start vagrant box" + box);
        }
    }

    public void halt() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("vagrant", "halt", box)
                .inheritIO()
                .directory(workingDirectory);
        Process p = pb.start();
        p.waitFor();
        if(p.exitValue() != 0) {
            throw new IOException("Unable to halt vagrant box" + box);
        }
    }

    @Override
    protected void after() {
        if (shutdown) {
            try {
                halt();
            }catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** A builder for the Vagrant rules. */
    public static class Builder {
        private final File workingDirectory;
        private String box = null;
        private boolean shutdown = true;

        private Builder(File workingDirectory) {
            this.workingDirectory = workingDirectory;
        }

        public static Builder workingDirectory(File workingDirectory) {
            return new Builder(workingDirectory);
        }

        public Builder leaveRunning() {
            shutdown = false;
            return this;
        }

        public Builder box(String box) {
            this.box = box;
            return this;
        }
        public VagrantRule build() {
            return new VagrantRule(workingDirectory, box, shutdown);
        }
    }
}
