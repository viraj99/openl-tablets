package org.openl.util;

import java.util.HashSet;
import java.util.Set;

import org.openl.types.IOpenClass;
import org.openl.types.NullOpenClass;
import org.openl.types.java.JavaOpenClass;

public final class OpenClassUtils {

    private OpenClassUtils() {
    }

    public static IOpenClass findParentClassWithBoxing(IOpenClass openClass1, IOpenClass openClass2) {
        IOpenClass t1 = openClass1;
        IOpenClass t2 = openClass2;
        if (t1.getInstanceClass() != null && t2.getInstanceClass() != null) {
            if ((t1.getInstanceClass().isPrimitive() && !t2.getInstanceClass().isPrimitive()) || (!t1.getInstanceClass()
                .isPrimitive() && t2.getInstanceClass().isPrimitive())) {
                if (t1.getInstanceClass().isPrimitive()) {
                    t1 = JavaOpenClass.getOpenClass(ClassUtils.primitiveToWrapper(t1.getInstanceClass()));
                }
                if (t2.getInstanceClass().isPrimitive()) {
                    t2 = JavaOpenClass.getOpenClass(ClassUtils.primitiveToWrapper(t2.getInstanceClass()));
                }
            }
        }
        return findParentClass(t1, t2);
    }
    
    public static IOpenClass findParentClass(IOpenClass class1, IOpenClass class2) {
        if (NullOpenClass.isAnyNull(class1)) {
            return class2;
        }
        if (NullOpenClass.isAnyNull(class2)) {
            return class1;
        }
        
        if (class1.isArray() && class2.isArray()) {
            int dim = 0;
            while (class1.isArray() && class2.isArray()) {
                dim++;
                class1 = class1.getComponentClass();
                class2 = class2.getComponentClass();
            }
            IOpenClass parentClass = findParentClass(class1, class2);
            if (parentClass == null) {
                return null;
            }
            return parentClass.getArrayType(dim);
        }

        if (class1.getInstanceClass() == null && class2.getInstanceClass() == null) {
            return class1;
        }
        
        //If class1 is NULL literal
        if (class1.getInstanceClass() == null) {
            if (class2.getInstanceClass().isPrimitive()) {
                return null;
            } else {
                return class2;
            }
        }

        //If class2 is NULL literal
        if (class2.getInstanceClass() == null) {
            if (class1.getInstanceClass().isPrimitive()) {
                return null;
            } else {
                return class1;
            }
        }
        
        if (class1.getInstanceClass().isPrimitive() || class2.getInstanceClass().isPrimitive()) { // If
                                                                                                  // one
                                                                                                  // is
                                                                                                  // primitive
            if (class1.equals(class2)) {
                return class1;
            }
            return null;
        }
        Set<IOpenClass> superClasses = new HashSet<IOpenClass>();
        IOpenClass openClass = class1;
        superClasses.add(openClass);
        while (!openClass.equals(JavaOpenClass.OBJECT)) {
            Iterable<IOpenClass> itr = openClass.superClasses();
            boolean f = false;
            for (IOpenClass superClass : itr) {
                if (!superClass.getInstanceClass().isInterface()) {
                    superClasses.add(superClass);
                    openClass = superClass;
                    f = true;
                    break;
                }
            }
            if (!f) {
                break;
            }
        }
        openClass = class2;
        if (superClasses.contains(class2)) {
            return class2;
        }
        while (!openClass.equals(JavaOpenClass.OBJECT)) {
            Iterable<IOpenClass> itr = openClass.superClasses();
            boolean f = false;
            for (IOpenClass superClass : itr) {
                if (!superClass.getInstanceClass().isInterface()) {
                    if (superClasses.contains(superClass)) {
                        return superClass;
                    }
                    openClass = superClass;
                    f = true;
                    break;
                }
            }
            if (!f) {
                break;
            }
        }
        return null;
    }

}