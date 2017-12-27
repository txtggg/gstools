package cst.gu.util.sql.test;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author guweichao 20171102
 * 
 */
public class TestServiceProxy implements MethodInterceptor {
	private Enhancer enhancer = new Enhancer();
	private Method method = null;
	
	public <T> T getProxy(T t) {
		Class<T> clazz = (Class<T>) t.getClass();
		return getProxy(clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> T getProxy(Class<T> clazz) {
		// 设置需要创建子类的类
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(this);
		// 通过字节码技术动态创建子类实例
		return (T) enhancer.create();
	}

	// 实现MethodInterceptor接口方法
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		if(this.method == null){
			System.out.println("first前置代理");
			this.method = method;
		}else{
			System.out.println("普通前置代理");
		}
		// 通过代理类调用父类中的方法
		Object result = proxy.invokeSuper(obj, args);
		if(this.method == method){
			System.out.println("first后置代理");
		}else{
			System.out.println("普通后置代理");
		}
		return result;
	}

}
