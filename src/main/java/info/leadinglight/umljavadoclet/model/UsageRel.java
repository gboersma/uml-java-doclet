package info.leadinglight.umljavadoclet.model;

/**
 * Generalization relationship.
 */
public class UsageRel extends ModelRel {
    public UsageRel(ModelClass src, ModelClass dest) {
        super(src, dest);
    }
    
    @Override
    public String getType() {
        return "uses";
    }
}
