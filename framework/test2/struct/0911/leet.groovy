import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class TopVotedCandidate {

    private int N
    private int[] times
    private int[] winner

    TopVotedCandidate(int[] persons, int[] times) {
        N = persons.length
        this.times = times
        winner = new int[persons.length]
        int[] counter = new int[N+1]
        int win = 0
        for (int i = 0; i < N; ++i) {
            if (++counter[persons[i]] >= counter[win]) {
                win = persons[i]
            }
            winner[i] = win
        }
    }
    
    int q(int t) {
        if (t >= times[times.length-1]) {
            return winner[N-1]
        }
        int low = 0, high = N-1
        while (low < high) {
            int mid = (low + high) / 2
            if (t <= times[mid]) {
                high = mid
            } else {
                low = mid + 1
            }
        }
        t == times[low] ? winner[low] : winner[low-1]
    }
}

// def work = TestWork.create(new Solution().&add)
def work = TestWork.forStruct(TopVotedCandidate)
// work.validator = { i, j -> i == j }
work.compareSerial = true
println work.run(System.in.getText('UTF-8'))
