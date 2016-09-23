package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.Type;

/**
 * A representation of a set of classes and the relationships between them.
 */
public class Model {
    public Model() {
        _classes = new ClassLookup();
        _classes.setModel(this);
        _packages = new PackageLookup();
        _packages.setModel(this);
    }
    
    public ClassLookup getClasses() {
        return _classes;
    }
    
    public ModelClass getClass(Type type) {
        return _classes.get(type);
    }
    
    public ModelClass getClass(String qualifiedName) {
        return _classes.get(qualifiedName);
    }
    
    public void addClass(ModelClass modelClass) {
        _classes.add(modelClass);
    }
    
    public PackageLookup getPackages() {
        return _packages;
    }
    
    public ModelPackage getPackage(PackageDoc packageDoc) {
       return _packages.get(packageDoc);
    }
    
    public ModelPackage getPackage(String qualifiedName) {
        return _packages.get(qualifiedName);
    }
    
    public void addPackage(ModelPackage modelPackage) {
        _packages.add(modelPackage);
    }
    
    public void addRelationship(ModelRel rel) {
        ModelClass src = rel.getSource();
        ModelClass dest = rel.getDestination();
        src.addRelationship(rel);
        // Only add the destination relationship if it is different than source.
        // Can be a relationship back to the same class.
        if (src != dest) {
            dest.addRelationship(rel);
        }
    }
    
    private final ClassLookup _classes;
    private final PackageLookup _packages;
}
