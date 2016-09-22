package info.leadinglight.umljavadoclet.model;

/**
 * Representation of a relationship in the model.
 */
public abstract class ModelRel extends ModelElement {
    public ModelRel(ModelClass src, ModelClass dest) {
        _src = src;
        _dest = dest;
    }
    
    public ModelClass getSource() {
        return _src;
    }
    
    public ModelClass getDestination() {
        return _dest;
    }
    
    public abstract String getType();
    
    private final ModelClass _src;
    private final ModelClass _dest;
}
