package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Maintains lookup of all classes in the model.
 */
public class TypeLookup extends ModelElement {
    public ModelType get(Type type) {
        return _types.get(type);
    }

    public List<ModelType> all() {
        return new ArrayList<>(_types.values());
    }
    
    public void add(ModelType modelType) {
        if (get(modelType.getType()) == null) {
            _types.put(modelType.getType(), modelType);
            modelType.setModel(getModel());
        }
    }
    
    public List<ModelType> internal() {
        ArrayList<ModelType> internal = new ArrayList<>();
        for (ModelType ModelType: all()) {
            if (ModelType.isInternal()) {
                internal.add(ModelType);
            }
        }
        return internal;
    }
    
    public List<ModelType> external() {
        ArrayList<ModelType> external = new ArrayList<>();
        for (ModelType modelType: all()) {
            if (modelType.isExternal()) {
                external.add(modelType);
            }
        }
        return external;
    }

    public ModelType createExternal(Type type) {
        ModelType modelType = get(type);
        if (modelType == null) {
            // This is a class that is outside the set of Javadoc root classes.
            // Add it to the model as an external class.
            modelType = new ModelType(type, false);
            add(modelType);
        }
        return modelType;
    }

    private final Map<Type,ModelType> _types = new LinkedHashMap<>();
}
