package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.PackageDoc;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Lookup of all packages in the model.
 */
public class PackageLookup extends ModelElement {
    public List<ModelPackage> getAll() {
        return new ArrayList<>(_packages.values());
    }
    
    public ModelPackage get(PackageDoc packageDoc) {
        return get(packageDoc.name());
    }
    
    public ModelPackage get(String qualifiedName) {
        return _packages.get(qualifiedName);
    }
    
    public void add(ModelPackage modelPackage) {
        if (get(modelPackage.getName()) == null) {
            modelPackage.setModel(getModel());
            _packages.put(modelPackage.getName(), modelPackage);
        }
    }
    
    private final Map<String,ModelPackage> _packages = new LinkedHashMap<>();
}
