import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class Solution {

    private int p

    NestedInteger deserialize(String s) {
        this.p = 0
        parse(s)
    }

    private NestedInteger parse(String s) {
        if (s[p] == '[') {
            ++p
            def root = new DefaultNestedInteger()
            while (s[p] != ']') {
                root.add(parse(s))
                if (s[p] == ',') {
                    ++p
                }
            }
            ++p
            return root
        }

        boolean negative = false
        if (s[p] == '-') {
            ++p
            negative = true
        }

        int value = 0
        while (p < s.length() && s[p] >= '0' && s[p] <= '9') {
            value = value * 10 + ((int)s[p]) - ((int)'0')
            ++p
        }

        if (negative) {
            value = 0 - value
        }
        new DefaultNestedInteger(value)
    }
}

def work = TestWork.create(new Solution().&deserialize)
// work.validator = { i, j -> i == j }
work.compareSerial = true
println work.run(System.in.getText('UTF-8'))
