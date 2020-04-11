package info.leadinglight.umljavadoclet.generics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestGenerics8<P extends ArrayList> extends HashMap<String,P> {
    public static class ListType {}
    // TODO Wildcards not displayed with bounds.
    public void addList(List<? extends ListType> list) {
    }
}
