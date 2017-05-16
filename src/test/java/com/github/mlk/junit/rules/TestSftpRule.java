package com.github.mlk.junit.rules;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.SecurityUtils;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.userauth.keyprovider.KeyPairWrapper;
import net.schmizz.sshj.xfer.FileSystemFile;
import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TemporaryFolder;

public class TestSftpRule {

  private TemporaryFolder sftpHome = new TemporaryFolder();
  public SftpRule subject = new SftpRule(sftpHome::getRoot);

  @Rule
  public RuleChain chain = RuleChain.outerRule(sftpHome).around(subject);
  @Rule
  public TemporaryFolder local = new TemporaryFolder();

  @Test
  public void uploadFile() throws IOException, NoSuchProviderException, NoSuchAlgorithmException {
    final SSHClient ssh = new SSHClient();
    ssh.addHostKeyVerifier(SecurityUtils.getFingerprint(subject.getPublicKey()));

    ssh.connect("localhost", subject.getPort());
    try {
      ssh.authPublickey("this_is_ignored", new KeyPairWrapper(
          KeyPairGenerator.getInstance("DSA", "SUN").generateKeyPair()));

      final SFTPClient sftp = ssh.newSFTPClient();
      File testFile = local.newFile("fred.txt");
      try {
        sftp.put(new FileSystemFile(testFile), "/fred.txt");
      } finally {
        sftp.close();
      }
    } finally {
      ssh.disconnect();
    }

    assertThat(sftpHome.getRoot().list(), is(new String[]{"fred.txt"}));
  }

  @Test
  public void listFiles() throws IOException, NoSuchProviderException, NoSuchAlgorithmException {
    sftpHome.newFile("fred.txt");
    List<RemoteResourceInfo> files;

    final SSHClient ssh = new SSHClient();
    ssh.addHostKeyVerifier(SecurityUtils.getFingerprint(subject.getPublicKey()));

    ssh.connect("localhost", subject.getPort());
    try {
      ssh.authPublickey("this_is_ignored", new KeyPairWrapper(
          KeyPairGenerator.getInstance("DSA", "SUN").generateKeyPair()));

      final SFTPClient sftp = ssh.newSFTPClient();

      try {
        files = sftp.ls("/");
      } finally {
        sftp.close();
      }
    } finally {
      ssh.disconnect();
    }

    assertThat(files.size(), is(1));
    assertThat(files.get(0).getName(), is("fred.txt"));
  }

  @Test
  public void downloadFile() throws IOException, NoSuchProviderException, NoSuchAlgorithmException {
    FileUtils.write(sftpHome.newFile("fred.txt"), "Electric boogaloo");

    final SSHClient ssh = new SSHClient();
    ssh.addHostKeyVerifier(SecurityUtils.getFingerprint(subject.getPublicKey()));

    ssh.connect("localhost", subject.getPort());
    try {
      ssh.authPublickey("this_is_ignored", new KeyPairWrapper(
          KeyPairGenerator.getInstance("DSA", "SUN").generateKeyPair()));

      final SFTPClient sftp = ssh.newSFTPClient();

      try {
        sftp.get("fred.txt", new FileSystemFile(new File(local.getRoot(), "fred.txt")));
      } finally {
        sftp.close();
      }
    } finally {
      ssh.disconnect();
    }

    assertThat(FileUtils.readFileToString(new File(local.getRoot(), "fred.txt")),
        is("Electric boogaloo"));
  }
}
