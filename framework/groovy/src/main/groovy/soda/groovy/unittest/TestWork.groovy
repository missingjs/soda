package soda.groovy.unittest

import org.codehaus.groovy.runtime.MethodClosure

import java.lang.reflect.Type
import java.util.function.BiPredicate

class TestWork<R> {

    private Closure<R> closure

    private List<Type> typeList

    TestWork(Closure<R> closure, List<Type> typeList) {
        this.closure = closure
        this.typeList = typeList
        System.err.println(typeList.toListString())
    }

    void setValidator(Closure<R> validator) {
        // TODO
        System.err.println(validator);
    }

    static <R> TestWork<R> create(MethodClosure mc) {
        def owner = mc.owner
        def methodName = mc.method
        def method = Utils.findMethod(owner.class, methodName)
        def argTypes = method.getGenericParameterTypes();
        def retType = method.getGenericReturnType()
        def typeList = [*argTypes]
        typeList << retType
        new TestWork<R>(mc, typeList)
    }

}
