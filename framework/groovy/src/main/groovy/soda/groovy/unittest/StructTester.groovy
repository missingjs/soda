package soda.groovy.unittest

import soda.groovy.unittest.conv.ConverterFactory
import soda.groovy.unittest.conv.ObjectConverter

class StructTester {

    private Class structClass

    StructTester(Class sc) {
        structClass = sc
    }

    Object test(List<String> operations, List<Object> parameters) {
        def ctor = structClass.getConstructors()[0]
        ctor.setAccessible(true)
        def cparams = Utils.parseArguments(
                ctor.getParameterTypes(),
                parameters.get(0) as List
        )
        def obj = ctor.newInstance(cparams)
        def res = [null]
        for (int i = 1; i < parameters.size(); ++i) {
            def method = Utils.findMethod(structClass, operations.get(i))
            method.setAccessible(true)
            def params = Utils.parseArguments(
                    method.getGenericParameterTypes(),
                    parameters.get(i) as List
            )
            def r = method.invoke(obj, params)
            ObjectConverter conv = ConverterFactory.create(method.getGenericReturnType())
            res << conv.toJsonSerializable(r)
        }
        res
    }

}
