package soda.groovy.unittest

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.codehaus.groovy.runtime.MethodClosure
import soda.groovy.unittest.conv.ConverterFactory
import soda.groovy.unittest.validate.FeatureFactory

import java.lang.reflect.Type
import java.util.function.BiPredicate

class TestWork<R> {

    private Closure<R> closure

    private List<Type> typeList

    private List<Type> argTypes

    private Type returnType

    boolean compareSerial

    Closure<Boolean> validator

    TestWork(Closure<R> closure, List<Type> typeList) {
        this.closure = closure
        argTypes = typeList[0..-1]
        returnType = typeList[-1]
    }

    String run(String text) {
        def input = new WorkInput(new JsonSlurper().parseText(text))
        def args = Utils.parseArguments(argTypes, input.args)

        def startNano = System.nanoTime()
        def result = closure.call(args.toArray(Object[]::new))
        def endNano = System.nanoTime()
        def elapseMillis = (endNano - startNano) / 1e6

        def output = new WorkOutput()
        output.id = input.id
        output.elapse = elapseMillis

        def resConv = ConverterFactory.create(returnType)
        output.result = resConv.toJsonSerializable(result)

        def success = true
        if (input.expected != null) {
            if (compareSerial && validator) {
                def a = JsonOutput.toJson(input.expected)
                def b = JsonOutput.toJson(output.result)
                success = (a == b)
            } else {
                def expect = resConv.fromJsonSerializable(input.expected)
                if (validator != null) {
                    success = validator.call(expect, result)
                } else {
                    success = FeatureFactory.create(returnType).isEqual(expect, result)
                }
            }
        }
        output.success = success
        JsonOutput.toJson(output)
    }

    static <R> TestWork<R> create(Closure<R> workClosure) {
        def mc = workClosure as MethodClosure
        def owner = mc.owner
        def methodName = mc.method
        def method = Utils.findMethod(owner.class, methodName)
        def argTypes = method.getGenericParameterTypes();
        def typeList = [*argTypes]
        def retType = method.getGenericReturnType()
        if (retType == void.class) {
            retType = typeList[0]
            def closure = { Object... args ->
                mc.call(args)
                args[0]
            }
            typeList << argTypes[0]
            new TestWork<R>((Closure<R>) closure, typeList)
        } else {
            typeList << retType
            new TestWork<R>(mc, typeList)
        }
    }

}
