package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.ClassDoc;

/**
 * Wrapper for class in the model that is part of javadoc classes (ClassDoc).
 */
public class InternalClass extends ModelClass {
    public InternalClass(ClassDoc classDoc) {
        _classDoc = classDoc;
    }
    
    public ClassDoc getClassDoc() {
        return _classDoc;
    }
    
    @Override
    public String getQualifiedName() {
        return _classDoc.qualifiedName();
    }
    
    private final ClassDoc _classDoc;
}
