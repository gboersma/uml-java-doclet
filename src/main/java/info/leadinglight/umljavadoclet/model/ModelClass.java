package info.leadinglight.umljavadoclet.model;

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
    
    public ModelRel getGeneralizationRelationship() {
        return _relLookup.getGeneralizationRel(this);
    }
    
    public void addRelationship(ModelRel rel) {
        _relLookup.addRelationship(rel);
    }
    
    @Override
    public void setModel(Model model) {
        super.setModel(model);
        _relLookup.setModel(model);
    }
    
    private final ModelRelLookup _relLookup = new ModelRelLookup();
}
