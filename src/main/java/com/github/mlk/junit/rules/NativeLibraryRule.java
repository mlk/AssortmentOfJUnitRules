package com.github.mlk.junit.rules;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.regex.Pattern;
import org.junit.rules.ExternalResource;
import org.junit.rules.TemporaryFolder;

/** Copies all the required native libraries from the classpath into a temp folder. */
public class NativeLibraryRule extends ExternalResource {
  private TemporaryFolder tempLibFolder;
  private final Pattern matcher;

  public NativeLibraryRule(Pattern matcher) {
    this.matcher = matcher;
    tempLibFolder = new TemporaryFolder();
  }

  public NativeLibraryRule() {
    this(Pattern.compile(".*"));
  }

  private void copyNativeLibraries() throws IOException {
    String classPath = System.getProperty("java.class.path", ".");
    final String[] classPathElements = classPath.split(System.getProperty("path.separator"));

    for(final String element : classPathElements) {
      String tmp = element.toLowerCase(Locale.UK);
      if (matcher.matcher(tmp).matches() && (tmp.endsWith("dll") || tmp.endsWith("so") || tmp.endsWith("dylib"))) {
        File currentFile = new File(element);
        Files.copy(Paths.get(currentFile.toURI()),
            Paths.get(new File(tempLibFolder.getRoot(), currentFile.getName()).toURI()));
      }
    }
  }

  public File getNativeLibrariesFolder() {
    return tempLibFolder.getRoot();
  }

  @Override
  protected void before() throws Throwable {
    tempLibFolder.create();
    copyNativeLibraries();
  }

  @Override
  protected void after() {
    tempLibFolder.delete();
  }
}
