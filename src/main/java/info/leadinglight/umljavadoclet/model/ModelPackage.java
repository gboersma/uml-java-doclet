package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.PackageDoc;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a package containing classes.
 */
public class ModelPackage {
    public ModelPackage(Model model, PackageDoc packageDoc) {
        _model = model;
        _packageDoc = packageDoc;
    }
    
    public void map() {
        // Nothing additional to map.
    }
    
    public String fullName() {
        return fullName(_packageDoc);
    }
    
    public String qualifiedName() {
        return fullName(_packageDoc);
    }
    
    public List<ModelClass> modelClasses() {
        return _classes;
    }
    
    public static String fullName(PackageDoc packageDoc) {
        return packageDoc.name();
    }
    
    // Update model
    
    public void addClass(ModelClass modelClass) {
        if (!_classes.contains(modelClass)) {
            _classes.add(modelClass);
        }
    }
    
    private final Model _model;
    private final PackageDoc _packageDoc;
    private final List<ModelClass> _classes = new ArrayList<>();
}
