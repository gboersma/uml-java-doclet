package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.Type;

/**
 * A representation of a set of classes and the relationships between them.
 */
public class Model {
    public Model() {
        _classLookup = new ModelClassLookup();
        _classLookup.setModel(this);
    }
    
    public ModelClassLookup getClassLookup() {
        return _classLookup;
    }
    
    public ModelClass getClass(Type type) {
        return _classLookup.getClass(type);
    }
    
    public ModelClass getClass(String qualifiedName) {
        return _classLookup.getClass(qualifiedName);
    }
    
    public void addClass(ModelClass modelClass) {
        _classLookup.addClass(modelClass);
    }
    
    public void addRelationship(ModelRel rel) {
        ModelClass src = rel.getSource();
        ModelClass dest = rel.getDestination();
        src.addRelationship(rel);
        // Only add the destination relationship if it is different than source.
        // Can be a relationship back to the same class.
        if (src != dest) {
            dest.addRelationship(rel);
        }
    }
    
    private final ModelClassLookup _classLookup;
}
