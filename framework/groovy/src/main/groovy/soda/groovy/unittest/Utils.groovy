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
        def args = []
        for (int i = 0; i < rawParams.size(); ++i) {
            args << ConverterFactory.create(types[i]).fromJsonSerializable(rawParams[i])
        }
        args
    }
}
