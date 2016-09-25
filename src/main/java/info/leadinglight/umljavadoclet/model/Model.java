package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A representation of a set of classes and the relationships between them.
 */
public class Model {
    public Model(RootDoc rootDoc) {
        _rootDoc = rootDoc;
    }
    
    public void mapToModel() {
        mapClasses();
    }
    
    public List<ModelClass> modelClasses() {
        return new ArrayList<>(_classes.values());
    }
    
    public ModelClass modelClass(String fullName) {
        return _classes.get(fullName);
    }
    
    public List<ModelPackage> modelPackages() {
        return new ArrayList<>(_packages.values());
    }
    
    public ModelPackage modelPackage(String fullName) {
        return _packages.get(fullName);
    }
    
    // Updating Model
    
    public ModelClass createClassIfNotExists(Type classType) {
        String fullName = ModelClass.fullName(classType);
        ModelClass modelClass = _classes.get(fullName);
        if (modelClass == null) {
            modelClass = new ModelClass(this, classType);
            _classes.put(fullName, modelClass);
        }
        return modelClass;
    }

    // Mapping
    
    private void mapClasses() {
        for (ClassDoc classDoc: _rootDoc.classes()) {
            ModelClass modelClass = mapClass(classDoc);
            ModelPackage modelPackage = mapPackage(classDoc.containingPackage());
            modelPackage.addClass(modelClass);
        }
    }
    
    private ModelClass mapClass(Type classType) {
        String fullName = ModelClass.fullName(classType);
        ModelClass modelClass = _classes.get(fullName);
        if (modelClass == null) {
            modelClass = new ModelClass(this, classType);
            _classes.put(fullName, modelClass);
            modelClass.mapToModel();
        }
        return modelClass;
    }
    
    private ModelPackage mapPackage(PackageDoc packageDoc) {
        String fullName = ModelPackage.fullName(packageDoc);
        ModelPackage modelPackage = _packages.get(fullName);
        if (modelPackage == null) {
            modelPackage = new ModelPackage(this, packageDoc);
            _packages.put(fullName, modelPackage);
            modelPackage.mapToModel();
        }
        return modelPackage;
    }

    private final RootDoc _rootDoc;
    private final Map<String,ModelClass> _classes = new LinkedHashMap<>();
    private final Map<String,ModelPackage> _packages = new LinkedHashMap<>();
}
