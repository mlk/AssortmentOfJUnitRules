package com.github.mlk.junit.rules;

import org.junit.ClassRule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class TestVagrantRuleMultiBoxExample {
    private static VagrantRule.Builder builder = VagrantRule.Builder.workingDirectory(getFile());
    @ClassRule
    public static VagrantRule box1 = builder.box("box1").build();
    @ClassRule
    public static VagrantRule box2 = builder.box("box2").build();

    @Test
    public void testVagrantRunning() throws URISyntaxException, IOException {
        // Pings a URL on the vagrant box.
        // The URL is in this case exposed via forwarded port.
        new URL("http://localhost:8481").openStream().close();
        new URL("http://localhost:8482").openStream().close();
    }


    public static File getFile() {
        try {
            return new File(TestVagrantRule.class.getResource("/vagrant/multimachine").toURI());
        } catch (URISyntaxException e) {
            return null;
        }
    }
}