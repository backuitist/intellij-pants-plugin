// Copyright 2014 Pants project contributors (see CONTRIBUTORS.md).
// Licensed under the Apache License, Version 2.0 (see LICENSE).

package com.twitter.intellij.pants.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.IncorrectOperationException;
import com.twitter.intellij.pants.util.PantsUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PantsPsiReferenceBase extends PsiReferenceBase<PsiElement> implements PsiReference {
  private final String myText;
  private final String myRelativePath;

  public PantsPsiReferenceBase(
    @NotNull PsiElement element,
    @NotNull TextRange range,
    @Nls String text,
    @Nls String relativePath
  ) {
    super(element, range, false);
    myText = text;
    myRelativePath = relativePath;
  }

  @Override
  @NotNull
  public String getCanonicalText() {
    return myText;
  }

  public String getText() {
    return myText;
  }

  public String getRelativePath() {
    return myRelativePath;
  }

  @Override
  public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
    return getElement();
  }

  @Override
  public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
    return getElement();
  }

  @Nullable
  protected VirtualFile findFile() {
    return findFile(myRelativePath);
  }

  @Nullable
  protected VirtualFile findFile(@NotNull String relativePath) {
    return PantsUtil.findFileRelativeToBuildRoot(myElement.getContainingFile(), relativePath);
  }
}
