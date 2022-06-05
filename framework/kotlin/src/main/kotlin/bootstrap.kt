
class __Bootstrap__ : (String) -> String {
    override fun invoke(text: String): String {
        TODO("Not yet implemented")
    }

}

fun main(args: Array<String>) {
    val input = generateSequence(::readLine).joinToString("\n")
    print(input)
}
