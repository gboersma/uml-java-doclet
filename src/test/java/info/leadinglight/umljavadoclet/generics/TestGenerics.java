package info.leadinglight.umljavadoclet.generics;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TestGenerics {

    Map<String, MyModel> result = new HashMap<>();

    public static void main(String[] args[]) {
        new TestGenerics().createValue("1").setValue("1VAL").build().createValue("2").setValue("2VAL").build().extendValue("1").setValue("1WAL-Override").build();
    }

    public MyModelBuilder<TestGenerics> createValue(String key) {
        return new MyModelBuilder<>(this, new MyModel(), (p, v) -> p.result.put(v.key, v)).setKey(key);
    }

    public MyModelBuilder<TestGenerics> extendValue(String key) {
        return new MyModelBuilder<>(this, result.get(key), (p, v) -> {
        });
    }

    public class MyModel {
        String key;
        String value;
    }

    public interface Builder<T> {
        T build();
    }

    /**
     * Abstract object builder with parent builder.
     *
     * @param <P> The parent builder.
     * @param <T> The object to build.
     * @param <B> The current builder.
     */
    abstract public class AbstractBuilder<P, T, B> implements Builder<P> {
        private final P parent;
        private final T value;
        private final BiConsumer<P, T> parentBuilder;

        public AbstractBuilder(P parent, T value, BiConsumer<P, T> parentBuilder) {
            this.parent = parent;
            this.value = value;
            this.parentBuilder = parentBuilder;
        }

        @Override
        public final P build() {
            parentBuilder.accept(parent, value);
            return parent;
        }

        protected final B make(Consumer<T> mixer) {
            mixer.accept(value);
            return (B) this;
        }
    }

    public class MyModelBuilder<P> extends AbstractBuilder<P, MyModel, MyModelBuilder<P>> {
        public MyModelBuilder(P parent, MyModel value, BiConsumer<P, MyModel> parentBuilder) {
            super(parent, value, parentBuilder);
        }

        protected MyModelBuilder<P> setKey(String key) {
            return make(v -> v.key = key);
        }

        public MyModelBuilder<P> setValue(String value) {
            return make(v -> v.value = value);
        }
    }
}
