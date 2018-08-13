package FactoryAndAOPTest;


import frame.Bean;
import proxy.action.AfterReturnAction;
import proxy.action.BeforeAction;
import proxy.action.CheckAction;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 测试AOP，实现AOP方法
 * 
 * @author 杨星辰
 *
 */
@Bean(name = "aopTest")
public class AopTest implements AfterReturnAction, BeforeAction, CheckAction {

	@Override
	public void beforeAction(Object target, Object... args) {
		System.out.print("AopTest beforeAction: target = " + target + "    arg =");
		checkArgs(args);
	}

	@Override
	public void afterReturnAction(Object ret, Object target, Object... args) {
		System.out.print("AopTest afterReturnAction: return=" + ret + " target = " + target + "    arg =");
		checkArgs(args);
	}

	@Override
	public boolean checkAction(Object target, Object... args) {
		System.out.print("AopTest checkAction: return = " + (0 != ((Object[]) args[0]).length) + "    target = "
				+ target + "    arg =");
		checkArgs(args);
		return 0 != ((Object[]) args[0]).length;
	}

	@Override
	public Object unCheckAction(Method[] UnCheckMethod, Object target, Object... args) {
		System.out.print("AopTest unCheckAction: target = " + target + "    arg =");
		checkArgs(args);
		System.out.print("\tmethod:");
		checkArgs(UnCheckMethod);
		return null;
	}

	private void checkArgs(Object[] args) {
		if (null != args) {
			Arrays.asList(args).forEach(es -> Arrays.asList(es).forEach(e -> {
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
