package org.openl.rules.lang.xls.binding.wrapper;

import java.util.Map;

import org.openl.binding.BindingDependencies;
import org.openl.rules.lang.xls.binding.ATableBoundNode;
import org.openl.rules.lang.xls.binding.XlsModuleOpenClass;
import org.openl.rules.lang.xls.syntax.TableSyntaxNode;
import org.openl.rules.method.table.MethodTableBoundNode;
import org.openl.rules.method.table.TableMethod;
import org.openl.rules.table.properties.ITableProperties;
import org.openl.types.IMemberMetaInfo;
import org.openl.types.IMethodSignature;
import org.openl.types.IOpenClass;
import org.openl.types.IOpenMethod;
import org.openl.types.IOpenMethodHeader;
import org.openl.types.impl.CompositeMethod;
import org.openl.vm.IRuntimeEnv;

public class TableMethodWrapper extends TableMethod implements IOpenMethodWrapper {
    TableMethod delegate;
    XlsModuleOpenClass xlsModuleOpenClass;

    public TableMethodWrapper(XlsModuleOpenClass xlsModuleOpenClass, TableMethod delegate) {
        this.delegate = delegate;
        this.xlsModuleOpenClass = xlsModuleOpenClass;
    }

    @Override
    public Object invoke(Object target, Object[] params, IRuntimeEnv env) {
        return WrapperLogic.invoke(this, target, params, env);
    }

    @Override
    public XlsModuleOpenClass getXlsModuleOpenClass() {
        return xlsModuleOpenClass;
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public IOpenClass getDeclaringClass() {
        return delegate.getDeclaringClass();
    }

    @Override
    public IOpenMethod getDelegate() {
        return delegate;
    }

    @Override
    public String getDisplayName(int mode) {
        return delegate.getDisplayName(mode);
    }

    @Override
    public IOpenMethodHeader getHeader() {
        return delegate.getHeader();
    }

    @Override
    public String getUri() {
        return delegate.getUri();
    }

    @Override
    public IOpenMethod getMethod() {
        return delegate.getMethod();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public IMethodSignature getSignature() {
        return delegate.getSignature();
    }

    @Override
    public IOpenClass getType() {
        return delegate.getType();
    }

    @Override
    public boolean isStatic() {
        return delegate.isStatic();
    }

    @Override
    public MethodTableBoundNode getMethodTableBoundNode() {
        return delegate.getMethodTableBoundNode();
    }

    @Override
    public BindingDependencies getDependencies() {
        return delegate.getDependencies();
    }

    @Override
    public String getSourceUrl() {
        return delegate.getSourceUrl();
    }

    @Override
    public CompositeMethod getCompositeMethod() {
        return delegate.getCompositeMethod();
    }

    @Override
    public void setBoundNode(ATableBoundNode node) {
        delegate.setBoundNode(node);
    }

    @Override
    public ATableBoundNode getBoundNode() {
        return delegate.getBoundNode();
    }

    @Override
    public Map<String, Object> getProperties() {
        return delegate.getProperties();
    }

    @Override
    public ITableProperties getMethodProperties() {
        return delegate.getMethodProperties();
    }

    @Override
    public IMemberMetaInfo getInfo() {
        return delegate.getInfo();
    }

    @Override
    public TableSyntaxNode getSyntaxNode() {
        return delegate.getSyntaxNode();
    }

    @Override
    public String getModuleName() {
        return delegate.getModuleName();
    }

    @Override
    public void setModuleName(String dependencyName) {
        delegate.setModuleName(dependencyName);
    }

    private TopClassOpenMethodWrapperCache topClassOpenMethodWrapperCache = new TopClassOpenMethodWrapperCache();

    @Override
    public IOpenMethod getTopOpenClassMethod(IOpenClass openClass) {
        return topClassOpenMethodWrapperCache.get(openClass);
    }

}
