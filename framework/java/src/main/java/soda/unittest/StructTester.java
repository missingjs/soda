package soda.unittest;

import soda.unittest.conv.ConverterFactory;
import soda.unittest.conv.ObjectConverter;

import java.util.ArrayList;
import java.util.List;

public class StructTester {

    private Class<?> structClass;

    public StructTester(Class<?> structClass) {
        this.structClass = structClass;
    }

    static TestWork createTestWork(Class<?> structClass) {
        return new TestWork(new StructTester(structClass), "test");
    }

    public Object test(List<String> operations, List<Object> parameters) {
        return Utils.wrapEx(() -> {
            var ctor = structClass.getConstructors()[0];
            ctor.setAccessible(true);
            var cparams = Utils.parseArguments(ctor.getParameterTypes(), Utils.cast(parameters.get(0)));
            var obj = ctor.newInstance(cparams);
            var res = new ArrayList<>();
            res.add(null);
            for (int i = 1; i < parameters.size(); ++i) {
                var method = Utils.findMethod(structClass, operations.get(i));
                method.setAccessible(true);
                var params = Utils.parseArguments(method.getGenericParameterTypes(), Utils.cast(parameters.get(i)));
                var r = method.invoke(obj, params);
                ObjectConverter<Object, Object> conv = ConverterFactory.create(method.getGenericReturnType());
                res.add(conv.toJsonSerializable(r));
            }
            return res;
        });
    }

}
