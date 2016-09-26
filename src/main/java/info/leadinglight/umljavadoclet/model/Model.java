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
    
    public void map() {
        mapClasses();
        mapRelationships();
    }
    
    public List<ModelClass> classes() {
        return new ArrayList<>(_classes.values());
    }
    
    public ModelClass modelClass(String fullName) {
        return _classes.get(fullName);
    }
    
    public List<ModelPackage> packages() {
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
            modelClass = new ModelClass(this, classType, false);
            modelClass.map();
            _classes.put(fullName, modelClass);
        }
        return modelClass;
    }

    // Mapping
    
    private void mapClasses() {
        // First add the classes and packages to the model.
        for (ClassDoc classDoc: _rootDoc.classes()) {
            ModelClass modelClass = new ModelClass(this, classDoc, true);
            String fullName = ModelClass.fullName(classDoc);
            _classes.put(fullName, modelClass);
            ModelPackage modelPackage = mapPackage(classDoc.containingPackage());
            modelPackage.addClass(modelClass);
        }
    }
    
    private void mapRelationships() {
        // Then map all the classes added to the model.
        for (ClassDoc classDoc: _rootDoc.classes()) {
            String fullName = ModelClass.fullName(classDoc);
            ModelClass modelClass = _classes.get(fullName);
            modelClass.map();
        }
    }
    
    private ModelPackage mapPackage(PackageDoc packageDoc) {
        String fullName = ModelPackage.fullName(packageDoc);
        ModelPackage modelPackage = _packages.get(fullName);
        if (modelPackage == null) {
            modelPackage = new ModelPackage(this, packageDoc);
            _packages.put(fullName, modelPackage);
            modelPackage.map();
        }
        return modelPackage;
    }

    private final RootDoc _rootDoc;
    private final Map<String,ModelClass> _classes = new LinkedHashMap<>();
    private final Map<String,ModelPackage> _packages = new LinkedHashMap<>();
}
