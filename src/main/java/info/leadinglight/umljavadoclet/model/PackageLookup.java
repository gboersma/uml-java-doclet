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
        return _packages.get(packageDoc);
    }
    
    public void add(ModelPackage modelPackage) {
        if (get(modelPackage.getPackageDoc()) == null) {
            modelPackage.setModel(getModel());
            _packages.put(modelPackage.getPackageDoc(), modelPackage);
        }
    }
    
    private final Map<PackageDoc,ModelPackage> _packages = new LinkedHashMap<>();
}
