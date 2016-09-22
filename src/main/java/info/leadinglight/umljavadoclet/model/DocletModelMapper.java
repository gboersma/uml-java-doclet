package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

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
    }
    
    private void mapSuperclass(ClassDoc classDoc) {
        if (classDoc.superclassType() != null) {
            String superclassName = classDoc.superclassType().qualifiedTypeName();
            // Do not include standard Java superclasses in the model.
            if (!superclassName.equals("java.lang.Object") && !superclassName.equals("java.lang.Enum")) {
                ModelClass source = _model.getClass(classDoc);
                ModelClass dest = _model.getClassLookup().createExternalClass(classDoc.superclassType());
                GeneralizationRel rel = new GeneralizationRel(source, dest);
                _model.addRelationship(rel);
            }
        }
    }
        
    private final Model _model = new Model();
}
