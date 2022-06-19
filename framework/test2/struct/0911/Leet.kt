import kotlin.math.*
import soda.kotlin.leetcode.*
import soda.kotlin.unittest.*

class TopVotedCandidate(val persons: IntArray, val times: IntArray) {

    private val N = persons.size
    private val winner = IntArray(N){0}

    init {
        val counter = IntArray(N+1){0}
        var win = 0
        for (i in 0 until N) {
            ++counter[persons[i]]
            if (counter[persons[i]] >= counter[win]) {
                win = persons[i]
            }
            winner[i] = win
        }
    }

    fun q(t: Int): Int {
        if (t >= times[times.size-1]) {
            return winner[N-1]
        }
        var (low, high) = listOf(0, N-1)
        while (low < high) {
            val mid = (low + high) / 2
            if (t <= times[mid]) {
                high = mid
            } else {
                low = mid + 1
            }
        }
        return if (t == times[low]) winner[low] else winner[low-1]
    }

}

class Leet : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        // val work = GenericTestWork.create(Solution()::add)

        // * by method that has not return value
        // val work = GenericTestWork.createVoid(Solution.METHOD_WITHOUT_RETURN)
        // * by class of data struct
        val work = GenericTestWork.forStruct(TopVotedCandidate::class)

        // * setup validator
        // work.validator = (T, T) -> Boolean
        work.compareSerial = true
        return work.run(text)
    }

}

fun main() {
    print(Leet()(Utils.fromStdin()))
}

