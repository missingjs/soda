package soda.groovy.unittest

import soda.groovy.unittest.conv.ConverterFactory

import java.lang.reflect.Method
import java.lang.reflect.Type

class Utils {

    static Method findMethod(Class<?> jobClass, String methodName) {
        def ms = jobClass.methods.grep{ it.name == methodName }
        if (ms) {
            return ms[0]
        }
        throw new NoSuchMethodException(methodName)
    }

    static List<Object> parseArguments(List<Type> types, List<Object> rawParams) {
        rawParams.eachWithIndex{ def param, int i ->
            ConverterFactory.create(types[i]).fromJsonSerializable(param)
        }
    }
}
