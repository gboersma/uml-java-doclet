package info.leadinglight.umljavadoclet.model;

/**
 * Any element that is part of a model.
 */
public class ModelElement {
    public Model getModel() {
        return _model;
    }
    
    public void setModel(Model model) {
        _model = model;
    }
    
    private Model _model;
}
