package factory;

public interface Factory<K,V> {
	/**
	 * 通过名得到对象
	 * @param name 名
	 * @return 目标对象
	 */
	public V get(final K key);
}
