/**
 * Created Jan 11, 2007
 */
package org.openl;

import java.util.List;
import java.util.Map;

import org.openl.message.OpenLMessage;
import org.openl.syntax.exception.CompositeSyntaxNodeException;
import org.openl.syntax.exception.SyntaxNodeException;
import org.openl.types.IOpenClass;

/**
 * Handles <code>{@link IOpenClass}</code> with parsing and compiled errors.
 * 
 * @author snshor
 * 
 */
public class CompiledOpenClass {

    private SyntaxNodeException[] parsingErrors;

    private SyntaxNodeException[] bindingErrors;

    private List<OpenLMessage> messages;

    private IOpenClass openClass;

    public CompiledOpenClass(IOpenClass openClass,
            List<OpenLMessage> messages,
            SyntaxNodeException[] parsingErrors,
            SyntaxNodeException[] bindingErrors) {
        
        this.openClass = openClass;
        this.parsingErrors = parsingErrors;
        this.bindingErrors = bindingErrors;
        this.messages = messages;
    }

    @Deprecated
    public SyntaxNodeException[] getBindingErrors() {
        return bindingErrors;
    }

    public IOpenClass getOpenClass() {
        throwErrorExceptionsIfAny();
        return openClass;
    }

    public IOpenClass getOpenClassWithErrors() {
        return openClass;
    }

    @Deprecated
    public SyntaxNodeException[] getParsingErrors() {
        return parsingErrors;
    }

    public boolean hasErrors() {
        return (parsingErrors.length > 0) || (bindingErrors.length > 0);
    }

    public void throwErrorExceptionsIfAny() {
        if (parsingErrors.length > 0) {
            throw new CompositeSyntaxNodeException("Parsing Error(s):", parsingErrors);
        }

        if (bindingErrors.length > 0) {
            throw new CompositeSyntaxNodeException("Binding Error(s):", bindingErrors);
        }

    }

    public List<OpenLMessage> getMessages() {
        return messages;
    }
    
    public Map<String, IOpenClass> getTypes() {
        return openClass.getTypes();
    }
    
}
