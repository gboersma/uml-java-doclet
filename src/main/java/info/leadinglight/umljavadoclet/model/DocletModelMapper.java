package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.ClassDoc;
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
        mapRootClasses(rootDoc);
        mapSuperclasses(rootDoc);
    }
    
    public void mapRootClasses(RootDoc rootDoc) {
        ClassDoc[] classes = rootDoc.classes();
        for (int i = 0; i < classes.length; ++i) {
            ClassDoc classDoc = classes[i];
            ModelInternalClass modelClass = new ModelInternalClass(classDoc);
            _model.getClassLookup().addClass(modelClass);
        }
    }
    
    public void mapSuperclasses(RootDoc rootDoc) {
        ClassDoc[] classes = rootDoc.classes();
        for (int i = 0; i < classes.length; ++i) {
            ClassDoc classDoc = classes[i];
            if (classDoc.superclassType() != null) {
                String superclassName = classDoc.superclassType().qualifiedTypeName();
                // Do not include standard Java superclasses in the model.
                if (!superclassName.equals("java.lang.Object") && !superclassName.equals("java.lang.Enum")) {
                    ModelClass source = getModelClass(classDoc);
                    ModelClass dest = createModelClass(classDoc.superclassType());
                    GeneralizationRel rel = new GeneralizationRel(source, dest);
                    _model.getRelationshipLookup().addRelationship(rel);
                }
            }
        }
    }
    
    private ModelClass getModelClass(Type type) {
        String className = type.qualifiedTypeName();
        ModelClass modelClass = _model.getClassLookup().getClass(className);
        return modelClass;
    }
    
    private ModelClass createModelClass(Type type) {
        ModelClass modelClass = getModelClass(type);
        if (modelClass == null) {
            // This is a class that is outside the set of Javadoc root classes.
            // Add it to the model as an external class.
            modelClass = new ModelExternalClass(type);
            _model.getClassLookup().addClass(modelClass);
        }
        return modelClass;
    }
        
    private final Model _model = new Model();
}
