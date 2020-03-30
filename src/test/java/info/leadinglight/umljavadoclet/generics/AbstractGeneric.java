package info.leadinglight.umljavadoclet.generics;

public abstract class AbstractGeneric<T> {
    public T getType() {
        return type;
    }

    public void setType(T type) {
        this.type = type;
    }

    private T type;
}
