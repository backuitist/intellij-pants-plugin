// Copyright 2016 Pants project contributors (see CONTRIBUTORS.md).
// Licensed under the Apache License, Version 2.0 (see LICENSE).

package com.twitter.intellij.pants.integration;

import com.intellij.openapi.util.text.StringUtil;
import com.twitter.intellij.pants.testFramework.PantsIntegrationTestCase;
import com.twitter.intellij.pants.testFramework.PantsTestUtils;
import com.twitter.intellij.pants.util.PantsConstants;
import com.twitter.intellij.pants.util.PantsUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class OSSPantsFromRootIntegrationTest extends PantsIntegrationTestCase {

  public static final String PROJECT_NAME = "some-project";

  @NotNull
  @Override
  protected List<File> getProjectFoldersToCopy() {
    final File testProjects = new File(PantsTestUtils.findTestPath("testData"), "testprojects");
    return Collections.singletonList(testProjects);
  }


  protected Path getPantsHome() {
    final String ossPantsHome = System.getenv("OSS_PANTS_HOME");
    assertFalse(StringUtil.isEmpty(ossPantsHome));
    return Paths.get(ossPantsHome);
  }

  @NotNull
  @Override
  protected File getProjectFolder() throws IOException {
    Path tmp = Files.createTempDirectory("pants");
    Path root = Files.createDirectory(tmp.resolve(PROJECT_NAME));
    Files.copy(getPantsHome().resolve(PantsConstants.PANTS), root.resolve(PantsConstants.PANTS));
    Files.copy(getPantsHome().resolve("build-support"), root.resolve("build-support"));
    Files.copy(getPantsHome().resolve("src"), root.resolve("src"));
    return root.toFile();
  }

  public void testRoot() {
    doImport("");

    assertProjectName("./::");
  }
}
