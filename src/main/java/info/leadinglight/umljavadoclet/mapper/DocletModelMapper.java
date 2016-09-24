package info.leadinglight.umljavadoclet.mapper;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.ParameterizedType;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Type;
import info.leadinglight.umljavadoclet.model.AssociationEndpoint;
import info.leadinglight.umljavadoclet.model.AssociationRel;
import info.leadinglight.umljavadoclet.model.DependencyRel;
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
        ModelClass modelClass = new ModelClass(classDoc, true);
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
        for (Type interfaceType: classDoc.interfaceTypes()) {
            ModelClass source = _model.getClass(classDoc);
                // If source class is an interface, than the relationship is a generalization,
                // not a realization.
            if (classDoc.isInterface()) {
                source.addGeneralizationTo(interfaceType);
            } else {
                source.addRealizationTo(interfaceType);
            }
        }
    }

    private void mapDependencies(ClassDoc classDoc) {
        mapFieldAssociations(classDoc);
        mapMethodDependencies(classDoc);
    }
    
    private void mapFieldAssociations(ClassDoc classDoc) {
        ModelClass source = _model.getClass(classDoc);
        for (FieldDoc fieldDoc: classDoc.fields()) {
            Type type = fieldDoc.type();
            AssociationRel association = mapTypeAssociation(source, type);
            if (association != null) {
                // TODO Multiplicity, determined by relationships through collection types.
                AssociationEndpoint endpoint = new AssociationEndpoint(fieldDoc.name(), null);
                if (source == association.getSource()) {
                    association.setDestinationEndpoint(endpoint);
                } else {
                    association.setSourceEndpoint(endpoint);
                }
            }
        }
    }

    private void mapMethodDependencies(ClassDoc classDoc) {
        ModelClass source = _model.getClass(classDoc);
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
    
    private DependencyRel mapTypeDependency(ModelClass src, Type type) {
        DependencyRel dependency = null;
        String typeName = type.qualifiedTypeName();
        // TODO Relationships through collection types.
        if (!type.simpleTypeName().equals("void") && !typeName.startsWith("java.lang.") && !type.isPrimitive()) {
            dependency = src.addDependencyTo(type);
        }
        return dependency;
    }
    
    private AssociationRel mapTypeAssociation(ModelClass src, Type type) {
        AssociationRel association = null;
        String typeName = type.qualifiedTypeName();
        // TODO Relationships through collection types.
        if (!type.simpleTypeName().equals("void") && !typeName.startsWith("java.lang.") && !type.isPrimitive()) {
            association = src.addAssociationTo(type);
        }
        return association;
    }
        
    private final Model _model = new Model();
}
