package soda.kotlin.unittest

import kotlinx.serialization.json.*
import soda.kotlin.unittest.conv.ConverterFactory
import kotlin.reflect.KClass
import kotlin.reflect.typeOf
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.primaryConstructor

class StructTester(private val structClass: KClass<*>) {
    fun test(operations: List<String>, parameters: JsonElement): JsonElement {
        val ctor = structClass.primaryConstructor ?:
            throw NoSuchMethodError("no constructor for class $structClass")
        val ctorParamTypes = ctor.parameters.map { it.type }
        val ctorParams = Utils.parseArguments(
                ctorParamTypes, parameters.jsonArray[0])
        val obj = ctor.call(*ctorParams.toTypedArray())
        val res = mutableListOf<JsonElement>()
        res.add(JsonNull)

        val methodMap = structClass.memberFunctions.associateBy { it.name }
        for (i in 1 until parameters.jsonArray.size) {
            val methodName = operations[i]
            val method = methodMap[methodName] ?:
                throw NoSuchMethodError("no member function $methodName for class $structClass")
            val paramTypes = method.parameters.drop(1).map { it.type }
            val params = Utils.parseArguments(paramTypes, parameters.jsonArray[i])
            val r = method.call(obj, *params.toTypedArray())
            val retType = method.returnType
            if (retType.toString() != typeOf<Unit>().toString()) {
                val conv = ConverterFactory.create<Any?>(retType)
                res.add(conv.toJsonSerializable(r))
            } else {
                res.add(JsonNull)
            }
        }
        return JsonArray(res)
    }
}
