/*
 * Created on Jul 24, 2003
 *
 * Developed by Intelligent ChoicePoint Inc. 2003
 */

package org.openl.conf;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;
import java.util.Stack;

import org.openl.source.DefaultDependencyManager;
import org.openl.source.IDependencyManager;

/**
 * @author snshor
 *
 */
public class UserContext extends AUserContext {

    static ThreadLocal<Stack<IUserContext>> contextStack = new ThreadLocal<Stack<IUserContext>>();

    protected ClassLoader userClassLoader;

    protected String userHome;

    protected Properties userProperties;
    
    protected IDependencyManager dependencyManager;

    public static IUserContext currentContext() {
        Stack<IUserContext> stack = contextStack.get();
        if (stack == null || stack.size() == 0) {
            return null;
        }
        return stack.peek();
    }

    public static IUserContext makeOrLoadContext(ClassLoader cl, String home) {
        IUserContext cxt = currentContext();
        if (cxt != null) {
            return cxt;
        }
        return new UserContext(cl, home);
    }

    public static void popCurrentContext() {
        contextStack.get().pop();
    }

    public static void pushCurrentContext(IUserContext cxt) {
        Stack<IUserContext> stack = contextStack.get();
        if (stack == null) {
            stack = new Stack<IUserContext>();
            contextStack.set(stack);
        }
        stack.push(cxt);
    }

    public UserContext(ClassLoader userClassLoader, String userHome) {
        this(userClassLoader, userHome, null, getDefaultDependencyManager());
    }

    private static IDependencyManager getDefaultDependencyManager() {        
        return new DefaultDependencyManager();
    }

    public UserContext(ClassLoader userClassLoader, String userHome, Properties userProperties) {
        this(userClassLoader, userHome, userProperties, getDefaultDependencyManager());
    }
    
    public UserContext(ClassLoader userClassLoader, String userHome, Properties userProperties, 
            IDependencyManager dependencyManager) {
        this.userClassLoader = userClassLoader;
        this.userHome = userHome;
        this.userProperties = userProperties;
        this.dependencyManager = dependencyManager;
    }

    public Object execute(IExecutable exe) {
        try {
            pushCurrentContext(this);
            return exe.execute();
        } finally {
            popCurrentContext();
        }
    }

    public ClassLoader getUserClassLoader() {
        return userClassLoader;
    }

    public String getUserHome() {
        return userHome;
    }

    public Properties getUserProperties() {
        return userProperties;
    }

    private String printClassloader(ClassLoader ucl) {
        if (ucl == null) {
            return "null";
        }
        if (ucl instanceof URLClassLoader) {
            URL[] urls = ((URLClassLoader) ucl).getURLs();
            StringBuilder sb = new StringBuilder();
            sb.append("ClassLoader URLs: ");
            for (URL url : urls) {
                sb.append(url.toExternalForm());
                sb.append(',');
            }
            return sb.toString();
        }
        return ucl.toString();
    }

    @Override
    public String toString() {
        return "home=" + userHome + " cl=" + printClassloader(userClassLoader);
    }

    public IDependencyManager getDependencyManager() {        
        return dependencyManager;
    }

    public void setDependencyManager(IDependencyManager dependencyManager) {        
        this.dependencyManager = dependencyManager;
    }

}
