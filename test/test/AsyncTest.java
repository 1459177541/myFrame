package test;

//import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import util.asynchronized.AsynAbstractExecutor;
import util.asynchronized.AsynResult;
import util.asynchronized.AsynResultExecutor;
import util.asynchronized.AsyncStaticExecuter;

import java.util.Random;

// import org.junit.Test;

/**
 * 测试线程池类
 * @author 杨星辰
 *
 */
public class AsyncTest {
	@Test
	public void test1() throws Exception{
		AsynResultExecutor<Integer> e1 = new AsynResultExecutor<>(new AsynResult<>() {
			@Override
			protected Integer execute() {
				int cont = 0;
				for (int i = 1; i <= 1000; i++) {
					cont+=i;
					if (i%100==0) {
						System.out.println(Thread.currentThread().getName()+"(e1)"+": "+cont);
					}
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.err.println("e1 complete!");
				return cont;
			}
		});
		e1.start();
		
		AsynResultExecutor<Double> e2 = new AsynResultExecutor<>(new AsynResult<>() {
			@Override
			protected Double execute() {
				double cont = 1;
				for (int i = 1; i <= 100; i++) {
					cont*=i;
					if (i%10==0) {
						System.out.println(Thread.currentThread().getName()+"(e2)"+": "+cont);
					}
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.err.println("e2 complete!");
				return cont;
			}
		});
		e2.start();
		
		AsynResult<String> e3 = AsyncStaticExecuter.startResult("hello world", a->{
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.err.println("e3 complete!");
			return a.toUpperCase();
		});
		
		AsyncStaticExecuter.start(()->{
			try {
				Thread.sleep(500);
			}catch (Exception ex) {
				ex.printStackTrace();
			}
			System.err.println("e4 complete!");
		});
		
		for(int i = 1; i<=30; i++) {
			AsyncStaticExecuter.start(i, e->{
				try {
					Thread.sleep(new Random().nextInt(500)+200);
				} catch (InterruptedException e4) {
					e4.printStackTrace();
				}
				System.err.println("E"+e+" complete!");
			});
		}
		

        AsyncStaticExecuter.start(()->{
            while (true) {
                try {
                    Thread.sleep(20);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.err.println("++++++执行:" + AsynAbstractExecutor.getExecuteSize() + " 等待:" + AsynAbstractExecutor.getWaitSize() + " 完成:" + AsynAbstractExecutor.getCompleteSize() + "++++++");
            }
		});
		System.err.println("e1 result: "+e1.getResult());
		System.err.println("e2 result: "+e2.getResult());
		System.err.println("e3 result: "+AsyncStaticExecuter.getResult(e3));
		Thread.sleep(3000);

	}

}
