// Copyright 2016 Pants project contributors (see CONTRIBUTORS.md).
// Licensed under the Apache License, Version 2.0 (see LICENSE).

package com.twitter.intellij.pants.refactor;

import com.twitter.intellij.pants.testFramework.PantsCodeInsightFixtureTestCase;

public class BuildFileRefactorTest extends PantsCodeInsightFixtureTestCase {


  public void testMoveDirectoryShouldUpdateAddresses() {
    setUpPantsExecutable();
    myFixture.addFileToProject("foo/bar/BUILD", "scala_library(name='bar')");
    myFixture.addFileToProject("foo/baz/BUILD", "scala_library(name='baz', dependencies=['foo/bar'])");
    myFixture.addFileToProject("BUILD", "scala_library(name='foo', dependencies=['foo/bar', 'foo/baz'])");

    // expectations
    myFixture.addFileToProject("BUILD.expected", "scala_library(name='foo', dependencies=['other/bar', 'other/baz'])");
    myFixture.addFileToProject("bazBUILD.expected", "scala_library(name='baz', dependencies=['other/bar'])");

    // TODO move directory
    // myFixture.moveFile("foo", "other");

    myFixture.checkResultByFile("BUILD", "BUILD.expected", false);
    myFixture.checkResultByFile("other/baz/BUILD", "bazBUILD.expected", false);
  }
}
