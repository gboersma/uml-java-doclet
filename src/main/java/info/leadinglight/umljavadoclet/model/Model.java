package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Type;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
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
        createPackages();
        mapPackages();
    }
    
    public List<ModelClass> classes() {
        return new ArrayList<>(_classes.values());
    }
    
    public List<ModelClass> internalClasses() {
        List<ModelClass> internalClasses = new ArrayList<>();
        for (ModelClass modelClass: classes()) {
            if (modelClass.isInternal()) {
                internalClasses.add(modelClass);
            }
        }
        return internalClasses;
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
    
    public ModelPackage parentPackage(ModelPackage childPackage) {
        String parentPackageName = childPackage.parentPackageFullName();
        if (parentPackageName != null) {
            return modelPackage(parentPackageName);
        } else {
            return null;
        }
    }
    
    public List<ModelPackage> childPackages(ModelPackage parentPackage) {
        List<ModelPackage> childPackages = new ArrayList<>();
        for (ModelPackage modelPackage: packages()) {
            if (modelPackage.isChildPackage(parentPackage)) {
                childPackages.add(modelPackage);
            }
        }
        return childPackages;
    }
    
    public List<ModelPackage> rootPackages() {
        List<ModelPackage> rootPackages = new ArrayList<>();
        for (ModelPackage modelPackage: packages()) {
            if (isRootPackage(modelPackage)) {
                rootPackages.add(modelPackage);
            }
        }
        return rootPackages;
    }
    
    public boolean isRootPackage(ModelPackage modelPackage) {
        // In order for a package to be a root package, no portion of the
        // package name may appear in the model.
        // Because of the way the doclet works, not all packages in scope are 
        // included in the model- only the ones that have package-summary or classes.
        String parentName = modelPackage.parentPackageFullName();
        while (parentName != null) {
            if (modelPackage(parentName) != null) {
                return false;
            }
            
            if (parentName.lastIndexOf(".") == -1) {
                return true;
            }
            
            parentName = parentName.substring(0, parentName.lastIndexOf("."));
        }
        return false;
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
    
    private void createPackages() {
        for (ClassDoc classDoc: _rootDoc.classes()) {
            PackageDoc packageDoc = classDoc.containingPackage();
            String fullName = ModelPackage.fullName(packageDoc);
            ModelPackage modelPackage = _packages.get(fullName);
            if (modelPackage == null) {
                modelPackage = new ModelPackage(this, packageDoc);
                _packages.put(fullName, modelPackage);
            }
            String classFullName = ModelClass.fullName(classDoc);
            ModelClass modelClass = _classes.get(classFullName);
            modelPackage.addClass(modelClass);
        }
    }
    
    public void mapPackages() {
        // Map the packages once all the classes are added.
        // They have all the info need for mapping relationships.
        for (ModelPackage modelPackage: _packages.values()) {
            modelPackage.map();
        }
    }

    private final RootDoc _rootDoc;
    private final Map<String,ModelClass> _classes = new LinkedHashMap<>();
    private final Map<String,ModelPackage> _packages = new LinkedHashMap<>();
}
