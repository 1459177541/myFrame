package factory;

public interface Factory {
	public Object get(final String name);
	public <T> T get(final Class<T> clazz);
}
