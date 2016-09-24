package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.Type;

/**
 * A representation of a set of classes and the relationships between them.
 */
public class Model {
    public Model() {
        _classes = new TypeLookup();
        _classes.setModel(this);
        _packages = new PackageLookup();
        _packages.setModel(this);
    }
    
    public TypeLookup getTypes() {
        return _classes;
    }
    
    public ModelType getType(Type type) {
        return _classes.get(type);
    }
    
    public void addType(ModelType modelType) {
        _classes.add(modelType);
    }
    
    public PackageLookup getPackages() {
        return _packages;
    }
    
    public ModelPackage getPackage(PackageDoc packageDoc) {
       return _packages.get(packageDoc);
    }
    
    public void addPackage(ModelPackage modelPackage) {
        _packages.add(modelPackage);
    }
    
    public void addRelationship(ModelRel rel) {
        ModelType src = rel.getSource();
        ModelType dest = rel.getDestination();
        src.addRelationship(rel);
        // Only add the destination relationship if it is different than source.
        // Can be a relationship back to the same class.
        if (src != dest) {
            dest.addRelationship(rel);
        }
    }
    
    private final TypeLookup _classes;
    private final PackageLookup _packages;
}
