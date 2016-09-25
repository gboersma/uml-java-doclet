package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ParameterizedType;
import com.sun.javadoc.Type;

/**
 * Represents a class internal or external to the model.
 */
public class ModelClass extends ModelElement {
    public ModelClass(Model model, Type type) {
        _model = model;
        _type = type;
    }
    
    public void mapToModel() {
        //mapRelationships();
    }
    
    public String fullName() {
        return fullName(_type);
    }
    
    public String qualifiedName() {
        return _type.qualifiedTypeName();
    }
    
    public boolean internal() {
        ClassDoc classDoc = _type.asClassDoc();
        return classDoc != null ? classDoc.isIncluded() : false;
    }
    
    public boolean external() {
        return !internal();
    }
    
    public static String fullName(Type type) {
        String params = buildParameterString(type);
        if (params.length() > 0) {
            return type.qualifiedTypeName() + "<" + params + ">";
        } else {
            return type.qualifiedTypeName();
        }
    }
    
    private static String buildParameterString(Type type) {
        StringBuilder sb = new StringBuilder();
        ParameterizedType paramType = type.asParameterizedType();
        if (paramType != null) {
            String sep = "";
            for (Type param : paramType.typeArguments()) {
                sb.append(sep);
                sb.append(param.simpleTypeName());
                sep = ", ";
            }
        }
        return sb.toString();
    }

    private final Model _model;
    private final Type _type;
}
