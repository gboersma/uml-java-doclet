package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.Type;
import java.util.List;

/**
 * Represents a class internal or external to the model.
 */
public abstract class ModelClass extends ModelElement {
    public abstract String getQualifiedName();
    
    public RelLookup getRelationshipLookup() {
        return _relLookup;
    }
    
    public List<ModelRel> getRelationships() {
        return _relLookup.all();
    }
    
    public void addRelationship(ModelRel rel) {
        _relLookup.add(rel);
    }
    
    public ModelRel getGeneralization() {
        return _relLookup.type(GeneralizationRel.class).source(this).first();
    }
    
    public void addGeneralizationTo(Type type) {
        ModelClass dest = getModel().getClasses().createExternal(type);
        GeneralizationRel rel = new GeneralizationRel(this, dest);
        getModel().addRelationship(rel);
    }
    
    public List<ModelRel> getDependencies() {
        return _relLookup.source(this).type(DependencyRel.class).all();
    }
    
    public List<ModelRel> getDependents() {
        return _relLookup.destination(this).type(DependencyRel.class).all();
    }

    public void addDependencyTo(Type type) {
        ModelClass dest = getModel().getClasses().createExternal(type);
        // Only add dependency to the class if a relationship does not already exist.
        if (dest != this && _relLookup.between(this, dest).isEmpty()) {
            DependencyRel rel = new DependencyRel(this, dest);
            getModel().addRelationship(rel);
        }
    }

    @Override
    public void setModel(Model model) {
        super.setModel(model);
        _relLookup.setModel(model);
    }
    
    public ModelPackage getPackage() {
        return _package;
    }
    
    public void setPackage(ModelPackage modelPackage) {
        _package = modelPackage;
    }
    
    private final RelLookup _relLookup = new RelLookup();
    private ModelPackage _package;
}
