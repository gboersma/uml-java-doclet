package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.Type;

/**
 * Wrapper to a type that is not part of the javadoc classes (Type).
 */
public class ExternalClass extends ModelClass {
    public ExternalClass(Type type) {
        _type = type;
    }
    
    public Type getType() {
        return _type;
    }
    
    @Override
    public String getQualifiedName() {
        return _type.qualifiedTypeName();
    }
    
    private final Type _type;
}
