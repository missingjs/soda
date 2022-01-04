package soda.unittest.work;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.BiPredicate;

import soda.unittest.work.parse.ObjectConverter;
import soda.unittest.work.parse.ConverterFactory;

public class TestWork {
	
	private Object solution;
	
	private Method method;
	
	private Type returnType;
	
	private Type[] argumentTypes;
	
	private final int numArguments;
	
	private TestLoader testLoader = new StdioTestLoader();
	
	private BiPredicate<?,?> validator;
	
	private boolean compareSerial = false;
	
	private WorkSerializer workSerializer = new JacksonWorkSerializer();

    private Object expectedOutput;

	public TestWork(Object su, String methodName) {
		this(su, findMethod(su.getClass(), methodName));
	}
	
	public TestWork(Object su, Method method) {
		this.solution = su;
		this.method = method;
		method.setAccessible(true);
		returnType = method.getGenericReturnType();
		argumentTypes = method.getGenericParameterTypes();
		numArguments = argumentTypes.length;
		validator = (a, b) -> { return a != null ? a.equals(b) : a == b; };
	}
	
	public void setTestLoader(TestLoader loader) {
		testLoader = loader;
	}
	
	public void setValidator(BiPredicate<?,?> v) {
		validator = v;
	}
	
	public void setCompareSerial(boolean b) {
		compareSerial = b;
	}
	
	public void setWorkSerializer(WorkSerializer ws) {
		workSerializer = ws;
	}

    public Object getExpectedOutput() {
        return expectedOutput;
    }
	
	@SuppressWarnings("unchecked")
	public void run() throws Exception {
		WorkInput input = workSerializer.parse(testLoader.load());
		Object[] arguments = parseArguments(argumentTypes, input.args);
		
		long startNano = System.nanoTime();
		Object result = method.invoke(solution, arguments);
		long endNano = System.nanoTime();
		double elapseMillis = (endNano - startNano) / 1e6;

		var resConv = (ObjectConverter<Object, Object>) ConverterFactory.createConverter(returnType);
		Object serialResult = resConv.toJsonSerializable(result);
		var output = new WorkOutput();
		output.id = input.id;
		output.result = serialResult;
		output.elapse = elapseMillis;
		
		boolean success = true;
		if (input.expected != null) {
			if (!compareSerial) {
				var expect = resConv.fromJsonSerializable(input.expected);
                expectedOutput = expect;
                success = ((BiPredicate<Object, Object>) validator).test(expect, result);
			} else {
				success = input.expected.equals(serialResult);
			}
		}
		output.success = success;
		testLoader.store(workSerializer.serialize(output));
	}
	
	@SuppressWarnings("unchecked")
	static Object[] parseArguments(Type[] types, List<Object> rawParams) throws Exception {
		Object[] arguments = new Object[types.length];
		for (int i = 0; i < arguments.length; ++i) {
			var conv = (ObjectConverter<Object, Object>) ConverterFactory.createConverter(types[i]);
			arguments[i] = conv.fromJsonSerializable(rawParams.get(i));
		}
		return arguments;
	}
	
	public static Method findMethod(Class<?> jobClass, String methodName) {
		Method[] methods = jobClass.getMethods();
		for (Method m : methods) {
			if (m.getName().equals(methodName)) {
				return m;
			}
		}
		throw new RuntimeException(new NoSuchMethodException(methodName));
	}

	public static TestWork forStruct(Class<?> structClass) {
		return StructTester.createTestWork(structClass);
	}
	
}
