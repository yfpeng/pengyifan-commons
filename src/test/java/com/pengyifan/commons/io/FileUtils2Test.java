package com.pengyifan.commons.io;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FileUtils2Test {

  @Rule
  public TemporaryFolder testFolder = new TemporaryFolder();

  private static final String STRING_WITH_NEWLINE = "abc\n";
  private static final String STRING_WITHOUT_NEWLINE = "cde";

  @Before
  public void setUp()
      throws Exception {
  }

  @Test
  public void testMergeFile()
      throws IOException {
    testHelper(
        STRING_WITH_NEWLINE + STRING_WITHOUT_NEWLINE,
        STRING_WITH_NEWLINE,
        STRING_WITHOUT_NEWLINE);
    testHelper(
        STRING_WITHOUT_NEWLINE + STRING_WITH_NEWLINE,
        STRING_WITHOUT_NEWLINE,
        STRING_WITH_NEWLINE);
    testHelper(
        STRING_WITH_NEWLINE + STRING_WITH_NEWLINE,
        STRING_WITH_NEWLINE,
        STRING_WITH_NEWLINE);
    testHelper(
        STRING_WITHOUT_NEWLINE + STRING_WITHOUT_NEWLINE,
        STRING_WITHOUT_NEWLINE,
        STRING_WITHOUT_NEWLINE);
  }

  private void testHelper(String expectedString, String str1, String str2)
      throws IOException {
    File destination = testFolder.newFile();

    File temp1 = testFolder.newFile();
    FileUtils.write(temp1, str1);

    File temp2 = testFolder.newFile();
    FileUtils.write(temp2, str2);

    FileUtils2.mergeFile(destination, Arrays.asList(temp1, temp2));
    String lines = FileUtils.readFileToString(destination);
    assertEquals(expectedString, lines);
  }
}
