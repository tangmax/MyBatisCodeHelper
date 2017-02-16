package com.ccnode.codegenerator.view;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReference;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @Author bruce.ge
 * @Date 2017/2/16
 * @Description
 */
public class PsiXmlAttributeReference  implements PsiReference{

    private XmlAttributeValue psiReference;

    private XmlAttributeValue psiElement;
    public PsiXmlAttributeReference(XmlAttributeValue element, XmlAttributeValue psiReference) {
        this.psiElement = element;
        this.psiReference = psiReference;
    }

    @Override
    public PsiElement getElement() {
        return this.psiElement;
    }

    @Override
    public TextRange getRangeInElement() {
        return new TextRange(0, this.psiElement.getTextLength());
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return psiReference;
    }

    @NotNull
    @Override
    public String getCanonicalText() {
        return psiReference.getText();
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        PsiElement parent = this.psiElement.getParent();
        if (!(parent instanceof XmlAttribute)) {
            return this.psiElement;
        } else {
            XmlAttribute attribute = (XmlAttribute) parent;
            attribute.setValue(newElementName);
            return attribute.getValueElement();
        }
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        return null;
    }

    @Override
    public boolean isReferenceTo(PsiElement element) {
        if (element instanceof PsiMethod) {
            return ((PsiMethod) element).getNameIdentifier() == resolve();
        }
        return false;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }

    @Override
    public boolean isSoft() {
        return false;
    }
}
