package factory;

public interface Factory {
	/**
	 * 通过名得到对象
	 * @param name 名
	 * @return 目标对象
	 */
	public Object get(final String name);
	public <T> T get(final Class<T> clazz);
}
