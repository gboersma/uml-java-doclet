package info.leadinglight.umljavadoclet.model;

/**
 * Information related to the end-point of an association.
 */
public class AssociationEndpoint {
    public AssociationEndpoint(String role, Multiplicity mult) {
        _role = role;
        _mult = mult;
    }
    
    public String getRole() {
        return _role;
    }
    
    public Multiplicity getMultiplicity() {
        return _mult;
    }
    
    private final String _role;
    private final Multiplicity _mult;
}
