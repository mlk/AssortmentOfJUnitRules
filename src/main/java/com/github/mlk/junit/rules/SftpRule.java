package com.github.mlk.junit.rules;

import static com.github.mlk.junit.rules.Helper.findRandomOpenPortOnAllLocalInterfaces;

import java.io.File;
import java.io.IOException;
import java.security.PublicKey;
import java.util.Collections;
import java.util.function.Supplier;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.common.keyprovider.ClassLoadableResourceKeyPairProvider;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.hostbased.StaticHostBasedAuthenticator;
import org.apache.sshd.server.auth.pubkey.AcceptAllPublickeyAuthenticator;
import org.apache.sshd.server.auth.pubkey.UserAuthPublicKeyFactory;
import org.apache.sshd.server.scp.ScpCommandFactory;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;
import org.junit.rules.ExternalResource;

public class SftpRule extends ExternalResource {
  private final Supplier<File> currentFolder;
  private SshServer sshd;

  public SftpRule(Supplier<File> currentFolder) {
    this.currentFolder = currentFolder;
  }

  @Override
  protected void before() throws Throwable {
    sshd = SshServer.setUpDefaultServer();
    sshd.setPort(findRandomOpenPortOnAllLocalInterfaces());
    sshd.setKeyPairProvider(new ClassLoadableResourceKeyPairProvider(getClass().getClassLoader(), "server_key"));
    sshd.setCommandFactory(new ScpCommandFactory());
    sshd.setSubsystemFactories(Collections.singletonList(new SftpSubsystemFactory()));
    sshd.setUserAuthFactories(Collections.singletonList(new UserAuthPublicKeyFactory()));
    sshd.setHost("localhost");
    sshd.setPublickeyAuthenticator(AcceptAllPublickeyAuthenticator.INSTANCE);
    sshd.setHostBasedAuthenticator(new StaticHostBasedAuthenticator(true));

    VirtualFileSystemFactory x = new VirtualFileSystemFactory();
    x.setDefaultHomeDir(currentFolder.get().toPath());

    sshd.setFileSystemFactory(x);
    sshd.start();
  }

  @Override
  protected void after() {
    try {
      sshd.stop();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public int getPort() {
    return sshd.getPort();
  }

  public PublicKey getPublicKey() {
    return sshd.getKeyPairProvider().loadKeys().iterator().next().getPublic();
  }
}
