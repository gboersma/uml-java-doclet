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
    public ModelClass get(String qualifiedName) {
        return _classes.get(qualifiedName);
    }
    
    public ModelClass get(Type type) {
        return ClassLookup.this.get(type.qualifiedTypeName());
    }

    public boolean has(String qualifiedName) {
        return ClassLookup.this.get(qualifiedName) != null;
    }
    
    public List<ModelClass> getAll() {
        return new ArrayList<>(_classes.values());
    }
    
    public void add(ModelClass modelClass) {
        if (ClassLookup.this.get(modelClass.getQualifiedName()) == null) {
            _classes.put(modelClass.getQualifiedName(), modelClass);
            modelClass.setModel(getModel());
        }
    }
    
    public ModelClass createExternal(Type type) {
        ModelClass modelClass = get(type);
        if (modelClass == null) {
            // This is a class that is outside the set of Javadoc root classes.
            // Add it to the model as an external class.
            modelClass = new ExternalClass(type);
            add(modelClass);
        }
        return modelClass;
    }

    private final Map<String,ModelClass> _classes = new LinkedHashMap<>();
}
