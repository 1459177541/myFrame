package frame;

public interface Factory<K,V> {
	/**
	 * 通过名得到对象
	 * @param key 名
	 * @return 目标对象
	 */
    V get(final K key);
}
