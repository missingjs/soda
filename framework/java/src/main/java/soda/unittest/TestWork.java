package soda.unittest;

import java.util.function.BiPredicate;

import soda.unittest.task.ReflectionTask;

public class TestWork {

	private final GenericTestWork<Object> gwork;

	public TestWork(Object su, String methodName) {
		gwork = new GenericTestWork<>(new ReflectionTask(su, methodName));
	}

	public void setCompareSerial(boolean b) {
		gwork.setCompareSerial(b);
	}
	
	public void setValidator(BiPredicate<?,?> v) {
		gwork.setValidator(Utils.cast(v));
	}

	public Object[] getArguments() {
		return gwork.getArguments();
	}

	public void run() {
		Utils.wrapEx(() -> System.out.println(run(Utils.fromStdin())));
	}

	public String run(String text) {
		return gwork.run(text);
	}

	public static TestWork forStruct(Class<?> structClass) {
		return StructTester.createTestWork(structClass);
	}

	public static TestWork forObject(Object obj, String method) {
		return new TestWork(obj, method);
	}
	
}
