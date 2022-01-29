package soda.unittest;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

import com.fasterxml.jackson.databind.ObjectMapper;
import soda.unittest.validate.ValidatorFactory;
import soda.unittest.conv.ObjectConverter;
import soda.unittest.conv.ConverterFactory;

public class TestWork {
	
	private Object solution;
	
	private Method method;
	
	private final Type returnType;
	
	private final Type[] argumentTypes;

	private Object[] arguments;

	private final Validatable<Object> validatable = new Validatable<>();

	public TestWork(Object su, String methodName) {
		this(su, Utils.findMethod(su.getClass(), methodName));
	}
	
	public TestWork(Object su, Method method) {
		this.solution = su;
		this.method = method;
		method.setAccessible(true);
		argumentTypes = method.getGenericParameterTypes();
		returnType = method.getGenericReturnType();
	}

	public void setCompareSerial(boolean b) {
		validatable.compareSerial = b;
	}
	
	public void setValidator(BiPredicate<?,?> v) {
		validatable.validator = Utils.cast(v);
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void run() {
		Utils.wrapEx(() -> System.out.println(run(Utils.fromStdin())));
	}

	public String run(String text) {
		try {
			return doRun(text);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private String doRun(String text) throws Exception {
		var input = Utils.objectMapper.readValue(text, WorkInput.class);
		arguments = Utils.parseArguments(argumentTypes, input.args);
		
		long startNano = System.nanoTime();
		var retType = returnType;
		Object result = method.invoke(solution, arguments);
		if (retType.equals(void.class)) {
			retType = argumentTypes[0];
			result = arguments[0];
		}
		long endNano = System.nanoTime();
		double elapseMillis = (endNano - startNano) / 1e6;

		var output = new WorkOutput();
		output.id = input.id;
		output.elapse = elapseMillis;
		validatable.validate(input, retType, result, output);
		return Utils.objectMapper.writeValueAsString(output);
	}

	public static TestWork forStruct(Class<?> structClass) {
		return StructTester.createTestWork(structClass);
	}

	public static TestWork forObject(Object obj, String method) {
		return new TestWork(obj, method);
	}
	
}
