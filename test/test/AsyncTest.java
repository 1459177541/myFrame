package test;

import org.junit.jupiter.api.Test;
import util.asynchronized.AsyncResult;
import util.asynchronized.AsyncAbstractExecutor;
import util.asynchronized.AsyncResultExecutor;
import util.asynchronized.AsyncStaticExecute;

import java.util.Random;

/**
 * 测试线程池类
 * @author 杨星辰
 *
 */
public class AsyncTest {
	@Test
	public void test1() throws Exception{
		AsyncResultExecutor<Integer> e1 = new AsyncResultExecutor<>(new AsyncResult<>() {
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

		AsyncResultExecutor<Double> e2 = new AsyncResultExecutor<>(new AsyncResult<>() {
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
		
		AsyncResult<String> e3 = AsyncStaticExecute.startResult("hello world", a->{
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.err.println("e3 complete!");
			return a.toUpperCase();
		});
		
		AsyncStaticExecute.start(()->{
			try {
				Thread.sleep(500);
			}catch (Exception ex) {
				ex.printStackTrace();
			}
			System.err.println("e4 complete!");
		});
		
		for(int i = 1; i<=30; i++) {
			AsyncStaticExecute.start(i, e->{
				try {
					Thread.sleep(new Random().nextInt(500)+200);
				} catch (InterruptedException e4) {
					e4.printStackTrace();
				}
				System.err.println("E"+e+" complete!");
			});
		}
		

        AsyncStaticExecute.start(()->{
            while (true) {
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.err.println(" 等待:" + AsyncAbstractExecutor.getWaitSize() + " 完成:" + AsyncAbstractExecutor.getCompleteSize() + "++++++");
            }
		});
		System.err.println("e1 result: "+e1.getResult());
		System.err.println("e2 result: "+e2.getResult());
		System.err.println("e3 result: "+AsyncStaticExecute.getResult(e3));
		Thread.sleep(3000);

	}

}
