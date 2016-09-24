package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.PackageDoc;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a package containing classes.
 */
public class ModelPackage extends ModelElement {
    public ModelPackage(PackageDoc packageDoc) {
        _packageDoc = packageDoc;
    }
    
    public PackageDoc getPackageDoc() {
        return _packageDoc;
    }
    
    public String getName() {
        return _packageDoc.name();
    }
    
    public List<ModelType> getClasses() {
        return _types;
    }
    
    public void addClass(ModelType modelType) {
        if (!_types.contains(modelType)) {
            _types.add(modelType);
        }
    }
    
    private final PackageDoc _packageDoc;
    private final List<ModelType> _types = new ArrayList<>();
}
