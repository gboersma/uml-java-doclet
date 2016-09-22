package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.Type;
import java.util.List;

/**
 * Represents a class internal or external to the model.
 */
public abstract class ModelClass extends ModelElement {
    public abstract String getQualifiedName();
    
    public ModelRelLookup getRelationshipLookup() {
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
    
    public ModelRel getUsageTo(ModelClass dest) {
        return _relLookup.getUsage(this, dest);
    }
    
    public List<ModelRel> getUsages() {
        return _relLookup.getUsages(this);
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
    
    public void addUsageTo(Type type) {
        ModelClass dest = getModel().getClassLookup().createExternalClass(type);
        if (dest != this && getUsageTo(dest) == null) {
            UsageRel rel = new UsageRel(this, dest);
            getModel().addRelationship(rel);
        }
    }

    @Override
    public void setModel(Model model) {
        super.setModel(model);
        _relLookup.setModel(model);
    }
    
    private final ModelRelLookup _relLookup = new ModelRelLookup();
}
