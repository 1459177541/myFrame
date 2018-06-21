package factory;

public interface Factory<S,R> {
	/**
	 * 通过名得到对象
	 * @param name 名
	 * @return 目标对象
	 */
	public R get(final S name);
}
