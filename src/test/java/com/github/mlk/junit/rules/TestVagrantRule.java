package com.github.mlk.junit.rules;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

public class TestVagrantRule {

    @Test
    public void testVagrantRunning() throws Throwable {
        VagrantRule subject = VagrantRule.Builder.workingDirectory(getFile()).build();
        try {
            subject.before();

            assertTrue(ping());

            subject.after();
        } finally {
            subject.halt();
        }
    }

    @Test
    public void testVagrantShutdownAfterRun() throws Throwable {
        VagrantRule subject = VagrantRule.Builder.workingDirectory(getFile()).build();
        try {
            subject.before();

            assumeTrue("The vagrant box should be running", ping());

            subject.after();

            assertFalse(ping());

        } finally {
            subject.halt();
        }
    }

    @Test
    public void testVagrantDoesNotShutdownAfterRun() throws Throwable {
        VagrantRule subject = VagrantRule.Builder.workingDirectory(getFile()).leaveRunning().build();
        try {
            subject.before();

            assumeTrue("The vagrant box should be running", ping());

            subject.after();

            assertTrue(ping());

        } finally {
            subject.halt();
        }
    }

    // Pings a URL on the vagrant box.
    // The URL is in this case exposed via forwarded port.
    private boolean ping() {
        try {
            new URL("http://localhost:8481").openStream().close();
        } catch(IOException e) {
            return false;
        }
        return true;
    }

    public static File getFile() {
        try {
            return new File(TestVagrantRule.class.getResource("/vagrant/singlemachine").toURI());
        } catch (URISyntaxException e) {
            return null;
        }
    }
}