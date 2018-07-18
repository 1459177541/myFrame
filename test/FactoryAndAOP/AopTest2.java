package FactoryAndAOP;

import frame.Bean;
import frame.proxy.action.AfterReturnAction;
import frame.proxy.action.BeforeAction;
import frame.proxy.action.CheckAction;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * 测试AOP，实现AOP方法
 * 
 * @author 杨星辰
 *
 */
@Bean(name = "aopTest")
public class AopTest2 implements AfterReturnAction, BeforeAction, CheckAction {

	@Override
	public void beforeAction(Object target, Object... args) {
		System.out.print("AopTest2 beforeAction: target = " + target + "    arg =");
		checkArgs(args);
	}

	@Override
	public void afterReturnAction(Object ret, Object target, Object... args) {
		System.out.print("AopTest2 afterReturnAction: return=" + ret + " target = " + target + "    arg =");
		checkArgs(args);
	}

	@Override
	public boolean checkAction(Object target, Object... args) {
		System.out.print("AopTest2 checkAction: return = " + (0 != ((Object[]) args[0]).length) + "    target = "
				+ target + "    arg =");
		checkArgs(args);
		return 0 != ((Object[]) args[0]).length;
	}

	@Override
	public Object unCheckAction(Method[] UnCheckMethod, Object target, Object... args) {
		System.out.print("AopTest2 unCheckAction: target = " + target + "    arg =");
		checkArgs(args);
		System.out.print("\tmethod:");
		checkArgs(UnCheckMethod);
		return null;
	}

	private void checkArgs(Object[] args) {
		if (null != args) {
			Stream.of(args).forEach(es -> Stream.of(es).forEach(e -> {
				if (e instanceof Object[]) {
					System.out.print(" " + Arrays.toString((Object[]) e));
				} else {
					System.out.print(" " + e);
				}
			}));
			System.out.println();
		} else {
			System.out.println("null");
		}
	}

}
