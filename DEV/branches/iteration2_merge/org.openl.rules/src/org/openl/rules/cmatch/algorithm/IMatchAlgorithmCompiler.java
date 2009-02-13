package org.openl.rules.cmatch.algorithm;

import org.openl.binding.IBindingContext;
import org.openl.rules.cmatch.ColumnMatch;

public interface IMatchAlgorithmCompiler {
    void compile(IBindingContext bindingContext, ColumnMatch columnMatch);
}