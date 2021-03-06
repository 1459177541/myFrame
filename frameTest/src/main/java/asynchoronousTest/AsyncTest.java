package asynchoronousTest;


import asynchronous.executor.AsyncAbstractExecutor;
import asynchronous.executor.AsyncExecuteManage;
import asynchronous.executor.AsyncLevel;
import asynchronous.executor.AsyncResultExecutor;
import asynchronous.model.AsyncResult;
import org.junit.jupiter.api.Test;

import java.util.Random;

/**
 * 测试线程池类
 * @author 杨星辰
 *
 */
public class AsyncTest {
	@SuppressWarnings({"deprecation", "InfiniteLoopStatement"})
    @Test
	public void test1() throws Exception{
		AsyncResultExecutor<Integer> e1 = new AsyncResultExecutor<>("Test1",new AsyncResult<>() {
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

		AsyncResultExecutor<Double> e2 = new AsyncResultExecutor<>("Test2",new AsyncResult<>() {
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
		
		AsyncResult<String> e3 = AsyncExecuteManage.startResult("hello world", a->{
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.err.println("e3 complete!");
			return a.toUpperCase();
		});
		
		AsyncExecuteManage.start(()->{
			try {
				Thread.sleep(500);
			}catch (Exception ex) {
				ex.printStackTrace();
			}
			System.err.println("e4 complete!");
		});
		
		for(int i = 1; i<=30; i++) {
			AsyncExecuteManage.start(i, e->{
				try {
					Thread.sleep(new Random().nextInt(500)+200);
				} catch (InterruptedException e4) {
					e4.printStackTrace();
				}
				System.err.println("E"+e+" complete!");
			});
		}
		

        AsyncExecuteManage.start(()->{
            while (true) {
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.err.println(" 等待:" + AsyncAbstractExecutor.getDefault().getWaitSize() + " 完成:" + AsyncAbstractExecutor.getDefault().getCompleteSize() + "++++++");
            }
		});
		System.err.println("e1 result: "+e1.waitAndGetResult());
		System.err.println("e2 result: "+e2.waitAndGetResult());
		System.err.println("e3 result: "+AsyncExecuteManage.waitAndGetResult(e3));
		Thread.sleep(3000);

	}

	/**
	 * 测试优先级
	 */
	@Test
	public void test2(){
		for (int i = 0; i<30; i++){
			AsyncExecuteManage.start(AsyncLevel.WAITABLE, i+1, t->{
				try{
					Thread.sleep(8*1000);
				}catch (Exception e){
					e.printStackTrace();
				}
				System.out.println("低优先线程 "+t+" 执行完毕！");
			});
			AsyncExecuteManage.start(AsyncLevel.NOW, i, t-> System.out.println("优先线程"+(t+1)+"执行完毕"));
			AsyncExecuteManage.start(i+1, t->{
				try{
					Thread.sleep(10*1000);
				}catch (Exception e){
					e.printStackTrace();
				}
				System.out.println("普通线程 "+t+" 执行完毕！");
			});
		}
		System.err.println(" 等待:" + AsyncAbstractExecutor.getDefault().getWaitSize() + " 完成:" + AsyncAbstractExecutor.getDefault().getCompleteSize() + "++++++");
		AsyncExecuteManage.start(AsyncLevel.WITHIN, ()-> System.out.println("WITHIN线程完成")).setWaitTime(3*1000);
		AsyncExecuteManage.start(AsyncLevel.NOW,()-> System.out.println("优先执行完毕"));
		System.err.println(" 等待:" + AsyncAbstractExecutor.getDefault().getWaitSize() + " 完成:" + AsyncAbstractExecutor.getDefault().getCompleteSize() + "++++++");
		try {
			Thread.sleep(50*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.err.println(" 等待:" + AsyncAbstractExecutor.getDefault().getWaitSize() + " 完成:" + AsyncAbstractExecutor.getDefault().getCompleteSize() + "++++++");
	}

}
