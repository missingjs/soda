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
	
	private Type returnType;
	
	private Type[] argumentTypes;
	
	private BiPredicate<?,?> validator;
	
	private boolean compareSerial = false;

    private Object expectedOutput;

	private Object[] arguments;

	public TestWork(Object su, String methodName) {
		this(su, findMethod(su.getClass(), methodName));
	}
	
	public TestWork(Object su, Method method) {
		this.solution = su;
		this.method = method;
		method.setAccessible(true);
		argumentTypes = method.getGenericParameterTypes();
		returnType = method.getGenericReturnType();
	}
	
	public void setValidator(BiPredicate<?,?> v) {
		validator = v;
	}
	
	public void setCompareSerial(boolean b) {
		compareSerial = b;
	}

    public Object getExpectedOutput() {
        return expectedOutput;
    }

	public Object[] getArguments() {
		return arguments;
	}

	public void run() {
		try {
			System.out.println(run(Utils.fromStdin()));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public String run(String text) {
		try {
			return doRun(text);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	private String doRun(String text) throws Exception {
		var input = Utils.objectMapper.readValue(text, WorkInput.class);
		arguments = parseArguments(argumentTypes, input.args);
		
		long startNano = System.nanoTime();
		var retType = returnType;
		Object result = method.invoke(solution, arguments);
		if (retType.equals(void.class)) {
			retType = argumentTypes[0];
			result = arguments[0];
		}
		long endNano = System.nanoTime();
		double elapseMillis = (endNano - startNano) / 1e6;

		var resConv = (ObjectConverter<Object, Object>) ConverterFactory.createConverter(retType);
		Object serialResult = resConv.toJsonSerializable(result);
		var output = new WorkOutput();
		output.id = input.id;
		output.result = serialResult;
		output.elapse = elapseMillis;
		
		boolean success = true;
		if (input.expected != null) {
			if (compareSerial && validator == null) {
				String a = Utils.objectMapper.writeValueAsString(input.expected);
				String b = Utils.objectMapper.writeValueAsString(serialResult);
				success = Objects.equals(a, b);
			} else {
				var expect = resConv.fromJsonSerializable(input.expected);
				expectedOutput = expect;
				if (validator != null) {
					success = ((BiPredicate<Object, Object>) validator).test(expect, result);
				} else {
					success = ValidatorFactory.create(retType).test(expect, result);
				}
			}
		}
		output.success = success;
		return Utils.objectMapper.writeValueAsString(output);
	}
	
	@SuppressWarnings("unchecked")
	public static Object[] parseArguments(Type[] types, List<Object> rawParams) {
		Object[] arguments = new Object[types.length];
		for (int i = 0; i < arguments.length; ++i) {
			var conv = (ObjectConverter<Object, Object>) ConverterFactory.createConverter(types[i]);
			arguments[i] = conv.fromJsonSerializable(rawParams.get(i));
		}
		return arguments;
	}

	public static List<Object> parseArguments(List<Type> types, List<Object> rawParams) {
		var res = parseArguments(types.toArray(new Type[0]), rawParams);
		return Arrays.asList(res);
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

	public static TestWork forObject(Object obj, String method) {
		return new TestWork(obj, method);
	}
	
}
