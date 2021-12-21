package soda.unittest.work;

import com.fasterxml.jackson.databind.ObjectMapper;
import soda.unittest.DataUtils;
import soda.unittest.work.parse.ParserFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class StructTester {

    private Class<?> structClass;

    private ObjectMapper mapper = new ObjectMapper();

    public StructTester(Class<?> structClass) {
        this.structClass = structClass;
    }

    static TestWork createTestWork(Class<?> structClass) {
        return new TestWork(new StructTester(structClass), "test");
    }

    public Object test(List<String> operations, List<Object> parameters) throws Exception {
        var ctor = structClass.getConstructors()[0];
        ctor.setAccessible(true);
        var cparams = parseArgs(ctor.getParameterTypes(), parameters.get(0));
        var obj = ctor.newInstance(cparams);
        var res = new ArrayList<Object>();
        res.add(null);
        for (int i = 1; i < parameters.size(); ++i) {
            var method = TestWork.findMethod(structClass, operations.get(i));
            method.setAccessible(true);
            var params = parseArgs(method.getParameterTypes(), parameters.get(i));
            var r = method.invoke(obj, params);
            res.add(r);
        }
        return res;
    }

    private Object[] parseArgs(Class<?>[] types, Object args) throws Exception {
        var objs = (List<Object>) args;
        var res = new Object[types.length];
        for (int i = 0; i < res.length; ++i) {
            var parser = ParserFactory.createParser(types[i]);
            res[i] = ((Function<Object, Object>) parser).apply(objs.get(i));
        }
        return res;
    }

}
