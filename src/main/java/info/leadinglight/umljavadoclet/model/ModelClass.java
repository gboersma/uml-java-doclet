package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ParameterizedType;
import com.sun.javadoc.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a class internal or external to the model.
 */
public class ModelClass {
    public ModelClass(Model model, Type type) {
        _model = model;
        _type = type;
        _classDoc = _type.asClassDoc();
    }
    
    public void mapToModel() {
        if (internal()) {
            // Only map internal classes.
            mapRelationships();
        }
    }
    
    public String fullName() {
        return fullName(_type);
    }
    
    public String qualifiedName() {
        return _type.qualifiedTypeName();
    }
    
    public boolean internal() {
        return _classDoc != null ? _classDoc.isIncluded() : false;
    }
    
    public boolean external() {
        return !internal();
    }
    
    public List<ModelRel> relationships() {
        return _rels;
    }
    
    public RelFilter relationshipsFilter() {
        return new RelFilter(_rels);
    }
    
    public ModelClass superclass() {
        ModelRel rel = relationshipsFilter().source(this).kind(ModelRel.Kind.GENERALIZATION).first();
        return rel != null ? rel.getDestination() : null;
    }
    
    public static String fullName(Type type) {
        String params = buildParameterString(type);
        if (params.length() > 0) {
            return type.qualifiedTypeName() + "<" + params + ">";
        } else {
            return type.qualifiedTypeName();
        }
    }
    
    // Update Model
    
    public void addRelationship(ModelRel rel) {
        _rels.add(rel);
    }
    
    // Mapping
    
    private void mapRelationships() {
        mapSuperclass();
//        mapInterfaces();
//        mapDependencies();
    }
    
    private void mapSuperclass() {
        Type superclassType = _classDoc.superclassType();
        if (superclassType != null) {
            String superclassName = superclassType.qualifiedTypeName();
            // Do not include standard Java superclasses in the model.
            if (!superclassName.equals("java.lang.Object") && !superclassName.equals("java.lang.Enum")) {
                ModelClass dest = _model.createClassIfNotExists(superclassType);
                ModelRel rel = new ModelRel(ModelRel.Kind.GENERALIZATION, this, dest);
                mapSourceRel(rel);
            }
        }
        
    }
    
    private void mapSourceRel(ModelRel rel) {
        _rels.add(rel);
        // Do not add relationships back to ourselves more than once.
        ModelClass dest = rel.getDestination();
        if (this != dest) {
            dest.addRelationship(rel);
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
    private final ClassDoc _classDoc;
    private final List<ModelRel> _rels = new ArrayList<>();
}
