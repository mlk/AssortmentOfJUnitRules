package com.github.mlk.junit.rules;

import org.junit.ClassRule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class TestVagrantRuleExample {
    @ClassRule
    public static VagrantRule subject = VagrantRule.Builder.workingDirectory(getFile()).build();

    @Test
    public void testVagrantRunning() throws URISyntaxException, IOException {
        // Pings a URL on the vagrant box.
        // The URL is in this case exposed via forwarded port.
        new URL("http://localhost:8481").openStream().close();
    }


    public static File getFile() {
        try {
            return new File(TestVagrantRule.class.getResource("/vagrant/singlemachine").toURI());
        } catch (URISyntaxException e) {
            return null;
        }
    }
}