package soda.scala.unittest.job

import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.lang.reflect.Modifier

import com.fasterxml.jackson.databind.ObjectMapper

import soda.unittest.job.InputData
import soda.unittest.job.OutputData

import soda.scala.unittest.codec.CodecFactory
import soda.scala.unittest.codec.ICodec

class JobExecutor {

    def exec(input: String, jspec: JobSpec): String = {
        val json = new ObjectMapper()
        val inputData = json.readValue(input, classOf[InputData])

        val args = new Array[Object](jspec.argClasses.length)
        for (i <- args.indices) {
            args(i) = decode(jspec.argClasses(i), inputData.arg(i))
        }

        var solution: Object = null
        val method = jspec.method
        method.setAccessible(true)
        if (!Modifier.isStatic(method.getModifiers)) {
            val ctor = jspec.jobClass.getDeclaredConstructor()
            ctor.setAccessible(true)
            solution = ctor.newInstance().asInstanceOf[Object]
        }

        val startNano = System.nanoTime
        val res = method.invoke(solution, args: _*)
        val endNano = System.nanoTime

        val elapseMillis = (endNano - startNano) / 1e6
        val serialRes = encode(jspec.retClass, res)

        val outputData = new OutputData()
        outputData.id = inputData.id
        outputData.result = serialRes
        outputData.elapse = elapseMillis

        var success = true
        if (inputData.hasExpected()) {
            if (jspec.validateByObject) {
                val expectObject = decode(jspec.retClass, inputData.expected)
                success = jspec.objectValidator.asInstanceOf[Validator[Object]].validate(expectObject, res)
            } else {
                success = jspec.serialValidator.asInstanceOf[Validator[Object]].validate(inputData.expected, serialRes)
            }
        }

        outputData.success = success
        json.writeValueAsString(outputData)
    }

    def encode(klass: Class[_], obj: Object): Object = {
        CodecFactory.create(klass).asInstanceOf[ICodec[Object,Object]].encode(obj)
    }

    def decode(klass: Class[_], ser: Object): Object = {
        CodecFactory.create(klass).asInstanceOf[ICodec[Object,Object]].decode(ser)
    }

}
