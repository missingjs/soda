import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class Solution {
    static class Info {
        int sum, product, maxDepth;
        Info(int s, int p, int m) {
            sum = s
            product = p
            maxDepth = m
        }
    }

    int depthSumInverse(List<NestedInteger> nestedList) {
        def info = getInfo(nestedList, 1)
        (info.maxDepth + 1) * info.sum - info.product
    }

    private Info getInfo(List<NestedInteger> nestedList, int depth) {
        int sum = 0, product = 0, maxDepth = depth
        for (ni in nestedList) {
            if (ni.isInteger()) {
                int val = ni.getInteger()
                sum += val
                product += val * depth
                maxDepth = Math.max(maxDepth, depth)
            } else {
                var res = getInfo(ni.getList(), depth+1)
                sum += res.sum
                product += res.product
                maxDepth = Math.max(maxDepth, res.maxDepth)
            }
        }
        new Info(sum, product, maxDepth)
    }
}

def work = TestWork.create(new Solution().&depthSumInverse)
// work.validator = { i, j -> i == j }
work.compareSerial = true
println work.run(System.in.getText('UTF-8'))
