package info.leadinglight.umljavadoclet.model;

/**
 * Representation of a relationship in the model.
 * Because of the nature of Java relationship between classes,
 * each relationship is directed from the source to the destination.
 * Any relationship from the destination to the source is 
 * represented with a different relationship.
 * This is not quite the correct model according to UML, since it
 * does not map correctly to associations. However, probably good
 * enough for the purpose of Javadoc diagrams.
 */
public class ModelRel {
    public ModelRel(Kind kind, ModelClass src, ModelClass dest) {
        _kind = kind;
        _src = src;
        _dest = dest;
        _srcRole = null;
        _srcCardinality = null;
    }

    public ModelRel(Kind kind, ModelClass src, ModelClass dest, String srcRole) {
        _kind = kind;
        _src = src;
        _dest = dest;
        _srcRole = srcRole;
        _srcCardinality = null;
    }

    public ModelRel(Kind kind, ModelClass src, ModelClass dest, String srcRole, Multiplicity srcCardinality) {
        _kind = kind;
        _src = src;
        _dest = dest;
        _srcRole = srcRole;
        _srcCardinality = srcCardinality;
    }
    
    public Kind getKind() {
        return _kind;
    }
    
    public ModelClass getSource() {
        return _src;
    }
    
    public String getSourceRole() {
        return _srcRole;
    }
    
    public Multiplicity getSourceCardinality() {
        return _srcCardinality;
    }

    public ModelClass getDestination() {
        return _dest;
    }
    
    public enum Kind {
        DIRECTED_ASSOCIATION, GENERALIZATION, REALIZATION, DEPENDENCY;
    }

    private final Kind _kind;
    private final ModelClass _src;
    private final ModelClass _dest;
    private final String _srcRole;
    private final Multiplicity _srcCardinality;
}
