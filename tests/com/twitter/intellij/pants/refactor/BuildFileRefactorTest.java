// Copyright 2016 Pants project contributors (see CONTRIBUTORS.md).
// Licensed under the Apache License, Version 2.0 (see LICENSE).

package com.twitter.intellij.pants.refactor;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.refactoring.move.MoveCallback;
import com.intellij.refactoring.move.moveFilesOrDirectories.MoveFilesOrDirectoriesProcessor;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import com.intellij.util.ArrayUtil;
import com.twitter.intellij.pants.testFramework.PantsCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

import java.io.IOException;

public class BuildFileRefactorTest extends PantsCodeInsightFixtureTestCase {


  public void testMoveDirectoryShouldUpdateAddresses() throws IOException {
    setUpPantsExecutable();
    myFixture.addFileToProject("foo/bar/BUILD", "scala_library(name='bar')");
    myFixture.addFileToProject("foo/baz/BUILD", "scala_library(name='baz', dependencies=['foo/bar'])");
    PsiFile rootBuildFile = myFixture.addFileToProject("BUILD", "scala_library(name='foo', dependencies=['foo/bar', 'foo/baz'])");
    myFixture.addFileToProject("other/DUMMY", ""); // to create the other directory

    // expectations
    myFixture.addFileToProject("BUILD.expected", "scala_library(name='foo', dependencies=['other/bar', 'other/baz'])");
    myFixture.addFileToProject("bazBUILD.expected", "scala_library(name='baz', dependencies=['other/bar'])");

    moveDirectory("foo/bar", "other");

    assertEquals(
      "scala_library(name='foo', dependencies=['other/bar', 'foo/baz'])",
      new String(rootBuildFile.getVirtualFile().contentsToByteArray()));
    //myFixture.checkResultByFile("BUILD", "BUILD.expected", false);
    //myFixture.checkResultByFile("foo/baz/BUILD", "bazBUILD.expected", false);
  }

  private void moveDirectory(@NotNull final String dirPath, @NotNull final String toPath) {
    final Project project = getProject();
    (new WriteCommandAction.Simple(project, new PsiFile[0]) {
      protected void run() throws Exception {
        VirtualFile virtualDir = myFixture.findFileInTempDir(dirPath);
        PsiDirectory directory = getPsiManager().findDirectory(virtualDir);

        VirtualFile virtualDirDest = myFixture.findFileInTempDir(toPath);
        PsiDirectory destDir = getPsiManager().findDirectory(virtualDirDest);

        boolean searchInNonJavaFiles = true;
        (new MoveFilesOrDirectoriesProcessor(project, new PsiElement[]{directory}, destDir, false, true /* search in non-java-files */,
                                             (MoveCallback)null, (Runnable)null)).run();
      }
    }).execute().throwException();
  }
}
