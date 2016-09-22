package info.leadinglight.umljavadoclet.model;

import java.util.ArrayList;
import java.util.List;

public class ModelRelFilter {
    public static List<ModelRel> filterForSourceClass(List<ModelRel> rels, ModelClass src) {
        List<ModelRel> filtered = new ArrayList<>();
        for (ModelRel rel: rels) {
            if (rel.getSource().getQualifiedName().equals(src.getQualifiedName())) {
                filtered.add(rel);
            }
        }
        return filtered;
    }

    public static List<ModelRel> filterForDestinationClass(List<ModelRel> rels, ModelClass dest) {
        List<ModelRel> filtered = new ArrayList<>();
        for (ModelRel rel: rels) {
            if (rel.getDestination().getQualifiedName().equals(dest.getQualifiedName())) {
                filtered.add(rel);
            }
        }
        return filtered;
    }
    
    public static List<ModelRel> filterForType(List<ModelRel> rels, Class<? extends ModelRel> relType) {
        List<ModelRel> filtered = new ArrayList<>();
        for (ModelRel rel: rels) {
            if (rel.getClass().equals(relType)) {
                filtered.add(rel);
            }
        }
        return filtered;
    }
    
    public static ModelRel filterFirstForType(List<ModelRel> rels, Class<? extends ModelRel> relType) {
        List<ModelRel> filtered = filterForType(rels, relType);
        return filtered.size() > 0 ? filtered.get(0) : null;
    }
}
