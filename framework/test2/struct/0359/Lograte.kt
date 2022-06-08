import soda.kotlin.unittest.*

class Logger() {

    private val msgMap = mutableMapOf<String, Int>()
    private var limit = 10
    private var lastTimestamp = -limit

    fun shouldPrintMessage(timestamp: Int, message: String): Boolean {
        val T = lastTimestamp
        lastTimestamp = timestamp
        if (timestamp - T >= limit) {
            msgMap.clear()
            msgMap[message] = timestamp
            return true
        }

        msgMap[message]?.let {
            if (timestamp - it < limit) {
                return false
            }
        }

        msgMap[message] = timestamp
        return true
    }

}

class Lograte : (String) -> String {
    override fun invoke(text: String): String {
        // val work = GenericTestWork.create(Solution()::add)

        // * by method that has not return value
        // val work = GenericTestWork.createVoid(Solution.METHOD_WITHOUT_RETURN)
        // * by class of data struct
        val work = GenericTestWork.forStruct(Logger::class)

        // * setup validator
        // work.validator = (T, T) -> Boolean
        work.compareSerial = true
        return work.run(text)
    }

}

fun main() {
    print(Lograte()(Utils.fromStdin()))
}
