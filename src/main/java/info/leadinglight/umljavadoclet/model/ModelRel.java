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
        _srcVisibility = null;
    }

    public ModelRel(Kind kind, ModelClass src, ModelClass dest, Visibility srcVisibility) {
        _kind = kind;
        _src = src;
        _dest = dest;
        _srcRole = null;
        _srcCardinality = null;
        _srcVisibility = srcVisibility;
    }

    public ModelRel(Kind kind, ModelClass src, ModelClass dest, String srcRole) {
        _kind = kind;
        _src = src;
        _dest = dest;
        _srcRole = srcRole;
        _srcCardinality = null;
        _srcVisibility = null;
    }

    public ModelRel(Kind kind, ModelClass src, ModelClass dest, String srcRole, Multiplicity srcCardinality) {
        _kind = kind;
        _src = src;
        _dest = dest;
        _srcRole = srcRole;
        _srcCardinality = srcCardinality;
        _srcVisibility = null;
    }
    
    public Kind kind() {
        return _kind;
    }
    
    public ModelClass source() {
        return _src;
    }
    
    public ModelClass destination() {
        return _dest;
    }

    public String destinationRole() {
        return _srcRole;
    }
    
    public Multiplicity destinationCardinality() {
        return _srcCardinality;
    }

    public Visibility destinationVisibility() {
        return _srcVisibility;
    }
    
    public void changeVisibility(Visibility visibility) {
        _srcVisibility = visibility;
    }
    
    public boolean isVisible(Visibility visibility) {
        if (_srcVisibility == null) {
            return true;
        } else {
            if (visibility == Visibility.PUBLIC && 
                    _srcVisibility == Visibility.PUBLIC) {
                return true;
            } else if (visibility == Visibility.PROTECTED && 
                    (_srcVisibility == Visibility.PUBLIC || _srcVisibility == Visibility.PROTECTED)) {
                return true;
            } else if (visibility == Visibility.PACKAGE &&
                    (_srcVisibility == Visibility.PUBLIC || _srcVisibility == Visibility.PROTECTED || _srcVisibility == Visibility.PACKAGE)) {
                return true;
            } else if (visibility == Visibility.PRIVATE) {
                return true;
            }
            return false;
        }
    }
    
    public enum Kind {
        DIRECTED_ASSOCIATION, GENERALIZATION, REALIZATION, DEPENDENCY;
    }
    
    public enum Multiplicity {
        ZERO_OR_ONE, ONE, MANY
    }
    
    public enum Visibility {
        PUBLIC, PROTECTED, PACKAGE, PRIVATE
    }

    private final Kind _kind;
    private final ModelClass _src;
    private final ModelClass _dest;
    private final String _srcRole;
    private final Multiplicity _srcCardinality;
    private Visibility _srcVisibility;
}
