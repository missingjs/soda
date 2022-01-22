package soda.scala.unittest.job

import java.lang.reflect.Method

class JobSpec(var jobClass: Class[_], val methodName: String) {

    val method: Method = findMethod(jobClass, methodName)

    var retClass: Class[_] = method.getReturnType()

    var argClasses: Array[Class[_]] = method.getParameterTypes()

    var validateByObject: Boolean = false

    var objectValidator: Validator[_] = new DefaultValidator()

    var serialValidator: Validator[_] = new DefaultValidator()

    private def findMethod(jobClass: Class[_], methodName: String): Method = {
        val mo = jobClass.getMethods().find(_.getName() == methodName)
        if (mo == None) {
            throw new NoSuchMethodException(methodName)
        }
        mo.get
    }

}
