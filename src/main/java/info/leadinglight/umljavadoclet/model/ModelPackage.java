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
    
    public List<ModelClass> getClasses() {
        return _classes;
    }
    
    public void addClass(ModelClass modelClass) {
        if (!_classes.contains(modelClass)) {
            _classes.add(modelClass);
        }
    }
    
    private final PackageDoc _packageDoc;
    private final List<ModelClass> _classes = new ArrayList<>();
}
