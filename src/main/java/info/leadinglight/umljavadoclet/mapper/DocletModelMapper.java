package info.leadinglight.umljavadoclet.mapper;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Type;
import info.leadinglight.umljavadoclet.model.InternalClass;
import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelPackage;

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
            mapPackage(classDoc);
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
        InternalClass modelClass = new InternalClass(classDoc);
        _model.addClass(modelClass);
    }
    
    public void mapPackage(ClassDoc classDoc) {
        ModelClass modelClass = _model.getClass(classDoc);
        ModelPackage modelPackage = _model.getPackage(classDoc.containingPackage());
        if (modelPackage == null) {
            modelPackage = new ModelPackage(classDoc.containingPackage());
            _model.addPackage(modelPackage);
        }
        modelPackage.addClass(modelClass);
        modelClass.setPackage(modelPackage);
    }
    
    public void mapClassRelationships(ClassDoc classDoc) {
        mapSuperclass(classDoc);
        mapInterfaces(classDoc);
        mapDependencies(classDoc);
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
    
    private void mapInterfaces(ClassDoc classDoc) {
        for (ClassDoc interfaceDoc: classDoc.interfaces()) {
            ModelClass source = _model.getClass(classDoc);
            // If source class is an interface, than the relationship is a generalization,
            // not a realization.
            if (classDoc.isInterface()) {
                source.addGeneralizationTo(interfaceDoc);
            } else {
                source.addRealizationTo(interfaceDoc);
            }
        }
    }

    private void mapDependencies(ClassDoc classDoc) {
        ModelClass source = _model.getClass(classDoc);
        // TODO Attributes
        for (MethodDoc methodDoc: classDoc.methods()) {
            if (methodDoc.isPublic()) {
                for (Parameter param: methodDoc.parameters()) {
                    Type type = param.type();
                    mapTypeDependency(source, type);
                }

                Type returnType = methodDoc.returnType();
                mapTypeDependency(source, returnType);
            }
        }
    }
    
    private void mapTypeDependency(ModelClass src, Type type) {
        String typeName = type.qualifiedTypeName();
        if (!type.simpleTypeName().equals("void") && !typeName.startsWith("java.lang.") && !type.isPrimitive()) {
            src.addDependencyTo(type);
        }
    }
        
    private final Model _model = new Model();
}
