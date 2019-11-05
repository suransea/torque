package top.srsea.torque.common;

import java.util.Objects;

public class NullableObject<T> {
    private T object;

    public NullableObject(T object) {
        this.object = object;
    }

    public static <T> NullableObject<T> of(T object) {
        return new NullableObject<>(object);
    }

    public static <T> NullableObject<T> nullOfClass(Class<T> objClass) {
        return new NullableObject<>(null);
    }

    public static <T> NullableObject<T> nullObject() {
        return new NullableObject<>(null);
    }

    public T get() {
        return object;
    }

    public boolean isNull() {
        return object == null;
    }

    public boolean isNotNull() {
        return object != null;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(object);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NullableObject)) {
            return false;
        }
        return Objects.equals(this, ((NullableObject) obj).object);
    }
}
