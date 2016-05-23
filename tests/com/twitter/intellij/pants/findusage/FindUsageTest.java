// Copyright 2016 Pants project contributors (see CONTRIBUTORS.md).
// Licensed under the Apache License, Version 2.0 (see LICENSE).

package com.twitter.intellij.pants.findusage;

import com.intellij.psi.PsiFile;
import com.twitter.intellij.pants.testFramework.PantsCodeInsightFixtureTestCase;

public class FindUsageTest extends PantsCodeInsightFixtureTestCase {

  public void testFindDirectoryUsage() {
    setUpPantsExecutable();
    PsiFile bar = myFixture.addFileToProject("foo/bar/BUILD", "");
    PsiFile baz = myFixture.addFileToProject("foo/baz/BUILD", "scala_library(name='baz', dependencies=['foo/bar'])");

    myFixture.addFileToProject("BUILD", "scala_library(name='foo', dependencies=['foo/bar', 'foo/baz'])");

    assertSize(2, myFixture.findUsages(bar));
    assertSize(1, myFixture.findUsages(baz));
  }
}
