package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Type;

/**
 * Populates the model based on the information provided by the doclet.
 */
public class DocletModelMapper {
    public Model getModel() {
        return _model;
    }
    
    public void map(RootDoc rootDoc) {
        mapClasses(rootDoc);
        mapRelationships(rootDoc);
    }
    
    public void mapClasses(RootDoc rootDoc) {
        ClassDoc[] classes = rootDoc.classes();
        for (int i = 0; i < classes.length; ++i) {
            ClassDoc classDoc = classes[i];
            mapClass(classDoc);
        }
    }
    
    public void mapRelationships(RootDoc rootDoc) {
        ClassDoc[] classes = rootDoc.classes();
        for (int i = 0; i < classes.length; ++i) {
            ClassDoc classDoc = classes[i];
            mapClassRelationships(classDoc);
        }
    }

    public void mapClass(ClassDoc classDoc) {
        ModelInternalClass modelClass = new ModelInternalClass(classDoc);
        _model.addClass(modelClass);
    }
    
    public void mapClassRelationships(ClassDoc classDoc) {
        mapSuperclass(classDoc);
        mapUsages(classDoc);
    }
    
    private void mapSuperclass(ClassDoc classDoc) {
        if (classDoc.superclassType() != null) {
            String superclassName = classDoc.superclassType().qualifiedTypeName();
            // Do not include standard Java superclasses in the model.
            if (!superclassName.equals("java.lang.Object") && !superclassName.equals("java.lang.Enum")) {
                ModelClass source = _model.getClass(classDoc);
                source.addGeneralizationTo(classDoc.superclassType());
            }
        }
    }
    
    private void mapUsages(ClassDoc classDoc) {
        ModelClass source = _model.getClass(classDoc);
        for (MethodDoc methodDoc: classDoc.methods()) {
            if (methodDoc.isPublic()) {
                mapMethodUsages(source, methodDoc);
            }
        }
    }
    
    private void mapMethodUsages(ModelClass src, MethodDoc methodDoc) {
        for (Parameter param: methodDoc.parameters()) {
            Type type = param.type();
            mapTypeUsage(src, type);
        }
        
        Type returnType = methodDoc.returnType();
        mapTypeUsage(src, returnType);
    }
    
    private void mapTypeUsage(ModelClass src, Type type) {
        String typeName = type.qualifiedTypeName();
        if (!type.simpleTypeName().equals("void") && !typeName.startsWith("java.lang.") && !type.isPrimitive()) {
            src.addUsageTo(type);
        }
    }
        
    private final Model _model = new Model();
}
