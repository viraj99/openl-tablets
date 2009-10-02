/*
 * Created on Nov 7, 2003
 *
 * Developed by Intelligent ChoicePoint Inc. 2003
 */

package org.openl.rules.dt;

import org.openl.IOpenSourceCodeModule;
import org.openl.OpenL;
import org.openl.binding.IBindingContextDelegator;
import org.openl.binding.impl.module.ModuleOpenClass;
import org.openl.types.IMethodSignature;
import org.openl.types.IOpenClass;
import org.openl.types.IOpenMethod;
import org.openl.types.IParameterDeclaration;

/**
 * @author snshor
 *
 */
public interface IDecisionRow {
    public DTParameterInfo findParameterInfo(String name);

    public IOpenMethod getMethod();

    public String getName();

    public DTParameterInfo getParameterInfo(int i);

    public String[] getParamPresentation();

    IParameterDeclaration[] getParams();

    // public IOpenMethod getCode();

    public Object[][] getParamValues();

    public IOpenSourceCodeModule getSourceCodeModule();

    public boolean isAction();

    public boolean isCondition();

    public int numberOfParams();

    public void prepare(IOpenClass methodType, IMethodSignature signature, OpenL openl, ModuleOpenClass dtModule,
            IBindingContextDelegator cxtd, RuleRow ruleRow) throws Exception;

}
