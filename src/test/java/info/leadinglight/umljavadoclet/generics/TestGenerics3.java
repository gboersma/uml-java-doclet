package info.leadinglight.umljavadoclet.generics;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TestGenerics3 {
    public Map<String, Optional<List<TestOption>>> getMap() {
        return map;
    }

    public void setMap(Map<String, Optional<List<TestOption>>> map) {
        this.map = map;
    }

    private Map<String, Optional<List<TestOption>>> map;
}
