package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.Type;
import java.util.List;

/**
 * A representation of a set of classes and the relationships between them.
 */
public class Model {
    public Model() {
        _classLookup = new ModelClassLookup();
        _classLookup.setModel(this);
        _relLookup = new ModelRelLookup();
        _relLookup.setModel(this);
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
    
    public ModelRelLookup getRelationshipLookup() {
        return _relLookup;
    }

    private final ModelClassLookup _classLookup;
    private final ModelRelLookup _relLookup;
}
