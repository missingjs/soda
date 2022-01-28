import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.TestWork;


class TopVotedCandidate {

    private int N;
    private int[] times;
    private int[] winner;

    public TopVotedCandidate(int[] persons, int[] times) {
        N = persons.length;
        this.times = times;
        winner = new int[persons.length];
        int[] counter = new int[N+1];
        int win = 0;
        for (int i = 0; i < N; ++i) {
            if (++counter[persons[i]] >= counter[win]) {
                win = persons[i];
            }
            winner[i] = win;
        }
    }
    
    public int q(int t) {
        if (t >= times[times.length-1]) {
            return winner[N-1];
        }
        int low = 0, high = N-1;
        while (low < high) {
            int mid = (low + high) / 2;
            if (t <= times[mid]) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return t == times[low] ? winner[low] : winner[low-1];
    }
}

public class Leet implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        // var work = new TestWork(new Solution(), "METHOD");
        var work = TestWork.forStruct(TopVotedCandidate.class);
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Leet().get().run();
    }

}
