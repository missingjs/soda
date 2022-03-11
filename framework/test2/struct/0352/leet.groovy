import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class SummaryRanges {

    private int[] parent

    private Set<Integer> ancestorSet = new HashSet<>()

    SummaryRanges() {
        parent = new int[10003]
    }
    
    void addNum(int val) {
        ++val

        if (parent[val] != 0) {
            return
        }

        parent[val] = -1
        ancestorSet.add(val)

        int left = val - 1, right = val + 1
        if (left > 0 && parent[left] != 0) {
            merge(left, val)
        }
        if (parent[right] != 0) {
            merge(val, right)
        }
    }

    public int[][] getIntervals() {
        def ans = new ArrayList<>(ancestorSet);
        Collections.sort(ans)
        int[][] res = new int[ans.size()][]
        for (int i = 0; i < res.length; ++i) {
            int start = ans.get(i)
            int end = start - parent[start] - 1
            res[i] = new int[] { start-1, end-1 }
        }
        res
    }

    private void merge(int x, int y) {
        int ax = getAncestor(x), ay = getAncestor(y)
        if (ax < ay) {
            mergeAncestor(ax, ay)
        } else {
            mergeAncestor(ay, ax)
        }
    }

    private void mergeAncestor(int ax, int ay) {
        parent[ax] += parent[ay]
        parent[ay] = ax
        ancestorSet.remove(ay)
    }

    private int getAncestor(int x) {
        parent[x] < 0 ? x : (parent[x] = getAncestor(parent[x]))
    }

}

// def work = TestWork.create(new Solution().&add)
def work = TestWork.forStruct(SummaryRanges)
// work.validator = { i, j -> i == j }
work.compareSerial = true
println work.run(System.in.getText('UTF-8'))
