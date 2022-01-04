package soda.unittest.work;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class StructTester {

    private Class<?> structClass;

    public StructTester(Class<?> structClass) {
        this.structClass = structClass;
    }

    static TestWork createTestWork(Class<?> structClass) {
        return new TestWork(new StructTester(structClass), "test");
    }

    public Object test(List<String> operations, List<Object> parameters) throws Exception {
        var ctor = structClass.getConstructors()[0];
        ctor.setAccessible(true);
        var cparams = TestWork.parseArguments(ctor.getParameterTypes(), (List<Object>) parameters.get(0));
        var obj = ctor.newInstance(cparams);
        var res = new ArrayList<Object>();
        res.add(null);
        for (int i = 1; i < parameters.size(); ++i) {
            var method = TestWork.findMethod(structClass, operations.get(i));
            method.setAccessible(true);
            var params = TestWork.parseArguments(method.getGenericParameterTypes(), (List<Object>) parameters.get(i));
            var r = method.invoke(obj, params);
            res.add(r);
        }
        return res;
    }

}
