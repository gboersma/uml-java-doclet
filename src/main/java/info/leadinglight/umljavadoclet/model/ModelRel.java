package info.leadinglight.umljavadoclet.model;

/**
 * Representation of a relationship in the model.
 */
public abstract class ModelRel extends ModelElement {
    public ModelRel(ModelType src, ModelType dest) {
        _src = src;
        _dest = dest;
    }
    
    public ModelType getSource() {
        return _src;
    }
    
    public ModelType getDestination() {
        return _dest;
    }
    
    private final ModelType _src;
    private final ModelType _dest;
}
