package soda.unittest.work;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

import soda.unittest.work.parse.ParserFactory;

public class TestWork {
	
	private Object solution;
	
	private Method method;
	
	private Class<?> returnType;
	
	private Class<?>[] argumentTypes;
	
	private final int numArguments;
	
	private TestLoader testLoader = new StdioTestLoader();
	
	private List<Function<?,?>> argumentParsers;
	
	private Function<?,?> resultSerializer;
	
	private Function<?,?> resultParser;
	
	private BiPredicate<?,?> validator;
	
	private boolean compareSerial = false;
	
	private WorkSerializer workSerializer = new JacksonWorkSerializer();

	public TestWork(Object su, String methodName) {
		this(su, findMethod(su.getClass(), methodName));
	}
	
	public TestWork(Object su, Method method) {
		this.solution = su;
		this.method = method;
		method.setAccessible(true);
		returnType = method.getReturnType();
		argumentTypes = method.getParameterTypes();
		numArguments = argumentTypes.length;
		argumentParsers = new ArrayList<>();
		for (int i = 0; i < numArguments; ++i) {
			argumentParsers.add(null);
		}
		try {
			resultSerializer = ParserFactory.createSerializer(returnType);
			resultParser = ParserFactory.createParser(returnType);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		validator = (a, b) -> { return a != null ? a.equals(b) : a == b; };
	}
	
	public void setTestLoader(TestLoader loader) {
		testLoader = loader;
	}
	
	public void setArgumentParser(int index, Function<?,?> parser) {
		argumentParsers.set(index, parser);
	}
	
	public void setResultParser(Function<?,?> parser) {
		resultParser = parser;
	}
	
	public void setResultSerializer(Function<?,?> s) {
		resultSerializer = s;
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
	
	@SuppressWarnings("unchecked")
	public void run() throws Exception {
		WorkInput input = workSerializer.parse(testLoader.load());
		Object[] arguments = parseArguments(input);
		
		long startNano = System.nanoTime();
		Object result = method.invoke(solution, arguments);
		long endNano = System.nanoTime();
		double elapseMillis = (endNano - startNano) / 1e6;

		Object serialResult = ((Function<Object,Object>)resultSerializer).apply(result);
		var output = new WorkOutput();
		output.id = input.id;
		output.result = serialResult;
		output.elapse = elapseMillis;
		
		boolean success = true;
		if (input.expected != null) {
			if (!compareSerial) {
				var expect = ((Function<Object,Object>)resultParser).apply(input.expected);
				success = ((BiPredicate<Object,Object>)validator).test(expect, result);
			} else {
				success = input.expected.equals(serialResult);
			}
		}
		output.success = success;
		testLoader.store(workSerializer.serialize(output));
	}
	
	@SuppressWarnings("unchecked")
	private Object[] parseArguments(WorkInput input) throws Exception {
		Object[] arguments = new Object[numArguments];
		for (int i = 0; i < arguments.length; ++i) {
			Function<?, ?> parser = argumentParsers.get(i);
			if (parser == null) {
				parser = ParserFactory.createParser(argumentTypes[i]);
			}
			arguments[i] = ((Function<Object, Object>) parser).apply(input.args.get(i));
		}
		return arguments;
	}
	
	private static Method findMethod(Class<?> jobClass, String methodName) {
		Method[] methods = jobClass.getMethods();
		for (Method m : methods) {
			if (m.getName().equals(methodName)) {
				return m;
			}
		}
		throw new RuntimeException(new NoSuchMethodException(methodName));
	}
	
}
