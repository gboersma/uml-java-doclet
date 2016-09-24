package info.leadinglight.umljavadoclet.model;

/**
 * Association relationship between classses.
 */
public class AssociationRel extends ModelRel {
    public AssociationRel(ModelClass src, ModelClass dest) {
        super(src, dest);
    }
    
    public AssociationEndpoint getSourceEndpoint() {
        return _srcEndpoint;
    }
    
    public boolean isSourceNavigable() {
        return _srcEndpoint != null;
    }
    
    public void setSourceEndpoint(AssociationEndpoint srcEndpoint) {
        _srcEndpoint = srcEndpoint;
    }

    public AssociationEndpoint getDestinationEndpoint() {
        return _destEndpoint;
    }
    
    public boolean isDestinationNavigable() {
        return _destEndpoint != null;
    }
    
    public void setDestinationEndpoint(AssociationEndpoint destEndpoint) {
        _destEndpoint = destEndpoint;
    }

    private AssociationEndpoint _srcEndpoint;
    private AssociationEndpoint _destEndpoint;
}
