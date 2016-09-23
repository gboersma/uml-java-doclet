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
        return _relLookup.getAll();
    }
    
    public List<ModelRel> getSourceRelationships() {
        return _relLookup.getForSource(this);
    }
    
    public List<ModelRel> getDestinationRelationships() {
        return _relLookup.getForDestination(this);
    }
    
    public ModelRel getGeneralization() {
        return _relLookup.getGeneralization(this);
    }
    
    public List<ModelRel> getGeneralized() {
        return _relLookup.getGeneralized(this);
    }
    
    public ModelRel getDependencyTo(ModelClass dest) {
        return _relLookup.getDependency(this, dest);
    }
    
    public List<ModelRel> getDependencies() {
        return _relLookup.getDependencies(this);
    }
    
    public List<ModelRel> getDependents() {
        return _relLookup.getDependents(this);
    }

    public void addRelationship(ModelRel rel) {
        _relLookup.add(rel);
    }
    
    public void addGeneralizationTo(Type type) {
        ModelClass dest = getModel().getClasses().createExternal(type);
        if (getGeneralization() == null) {
            GeneralizationRel rel = new GeneralizationRel(this, dest);
            getModel().addRelationship(rel);
        }
    }
    
    public void addDependencyTo(Type type) {
        ModelClass dest = getModel().getClasses().createExternal(type);
        if (dest != this && getDependencyTo(dest) == null) {
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
