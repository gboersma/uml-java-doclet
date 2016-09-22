package info.leadinglight.umljavadoclet.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Maintains lookup of all classes in the model.
 */
public class ModelClassLookup extends ModelElement {
    public ModelClass getClass(String qualifiedName) {
        return _classes.get(qualifiedName);
    }
    
    public boolean hasClass(String qualifiedName) {
        return getClass(qualifiedName) != null;
    }
    
    public List<ModelClass> getClasses() {
        return new ArrayList<ModelClass>(_classes.values());
    }
    
    public void addClass(ModelClass modelClass) {
        if (getClass(modelClass.getQualifiedName()) == null) {
            _classes.put(modelClass.getQualifiedName(), modelClass);
            modelClass.setModel(getModel());
        }
    }

    private final Map<String,ModelClass> _classes = new HashMap<String,ModelClass>();
}
