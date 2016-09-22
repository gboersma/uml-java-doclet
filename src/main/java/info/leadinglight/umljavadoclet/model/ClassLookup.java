package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Maintains lookup of all classes in the model.
 */
public class ClassLookup extends ModelElement {
    public ModelClass getClass(String qualifiedName) {
        return _classes.get(qualifiedName);
    }
    
    public ModelClass getClass(Type type) {
        return getClass(type.qualifiedTypeName());
    }

    public boolean hasClass(String qualifiedName) {
        return getClass(qualifiedName) != null;
    }
    
    public List<ModelClass> getClasses() {
        return new ArrayList<>(_classes.values());
    }
    
    public void addClass(ModelClass modelClass) {
        if (getClass(modelClass.getQualifiedName()) == null) {
            _classes.put(modelClass.getQualifiedName(), modelClass);
            modelClass.setModel(getModel());
        }
    }
    
    public ModelClass createExternalClass(Type type) {
        ModelClass modelClass = getClass(type);
        if (modelClass == null) {
            // This is a class that is outside the set of Javadoc root classes.
            // Add it to the model as an external class.
            modelClass = new ExternalClass(type);
            addClass(modelClass);
        }
        return modelClass;
    }

    private final Map<String,ModelClass> _classes = new LinkedHashMap<>();
}
