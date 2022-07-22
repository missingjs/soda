using Soda.Unittest;
using Soda.Leetcode;

public class TopVotedCandidate {

    private int N;

    private int[] times, winner;

    public TopVotedCandidate(int[] persons, int[] times) {
        N = persons.Length;
        this.times = times;
        winner = new int[persons.Length];
        int[] counter = new int[N+1];
        int win = 0;
        for (int i = 0; i < N; ++i) {
            if (++counter[persons[i]] >= counter[win]) {
                win = persons[i];
            }
            winner[i] = win;
        }
    }
    
    public int Q(int t) {
        if (t >= times[times.Length-1]) {
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

public class Leet
{
    public static void Main(string[] args)
    {
        // var work = WorkFactory.Create(Utils.Fn(new Solution().Add));
        var work = WorkFactory.ForStruct<TopVotedCandidate>();
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.ReadStdin()));
    }
}
