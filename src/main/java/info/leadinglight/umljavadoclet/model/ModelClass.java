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
    
    public List<ModelRel> getSourceRelationships() {
        return _relLookup.getRelationshipsForSource(this);
    }
    
    public List<ModelRel> getDestinationRelationships() {
        return _relLookup.getRelationshipsForDestination(this);
    }
    
    public ModelRel getGeneralization() {
        return _relLookup.getGeneralization(this);
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
        _relLookup.addRelationship(rel);
    }
    
    public void addGeneralizationTo(Type type) {
        ModelClass dest = getModel().getClassLookup().createExternalClass(type);
        if (getGeneralization() == null) {
            GeneralizationRel rel = new GeneralizationRel(this, dest);
            getModel().addRelationship(rel);
        }
    }
    
    public void addDependencyTo(Type type) {
        ModelClass dest = getModel().getClassLookup().createExternalClass(type);
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
    
    private final RelLookup _relLookup = new RelLookup();
}
