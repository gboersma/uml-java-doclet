package info.leadinglight.umljavadoclet.generics;

import java.util.List;
import java.util.Optional;

public class TestGenerics2 {
    public List<Optional<TestOption>> getOptionals() {
        return optionals;
    }

    public void setOptionals(List<Optional<TestOption>> optionals) {
        this.optionals = optionals;
    }

    private List<Optional<TestOption>> optionals;
}
