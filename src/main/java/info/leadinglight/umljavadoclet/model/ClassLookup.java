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
    public ModelClass get(Type type) {
        return _classes.get(type);
    }

    public List<ModelClass> all() {
        return new ArrayList<>(_classes.values());
    }
    
    public void add(ModelClass modelClass) {
        if (get(modelClass.getType()) == null) {
            _classes.put(modelClass.getType(), modelClass);
            modelClass.setModel(getModel());
        }
    }
    
    public List<ModelClass> internal() {
        ArrayList<ModelClass> internal = new ArrayList<>();
        for (ModelClass modelClass: all()) {
            if (modelClass.isInternal()) {
                internal.add(modelClass);
            }
        }
        return internal;
    }
    
    public List<ModelClass> external() {
        ArrayList<ModelClass> external = new ArrayList<>();
        for (ModelClass modelClass: all()) {
            if (modelClass.isExternal()) {
                external.add(modelClass);
            }
        }
        return external;
    }

    public ModelClass createExternal(Type type) {
        ModelClass modelClass = get(type);
        if (modelClass == null) {
            // This is a class that is outside the set of Javadoc root classes.
            // Add it to the model as an external class.
            modelClass = new ModelClass(type, false);
            add(modelClass);
        }
        return modelClass;
    }

    private final Map<Type,ModelClass> _classes = new LinkedHashMap<>();
}
