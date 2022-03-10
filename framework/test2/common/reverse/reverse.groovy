import soda.groovy.unittest.*

class Solution {
    String reverseVowels(String s) {
        def isv = new boolean[128]
        def vs = "aeiouAEIOU"
        vs.each { isv[(int) it] = true }
        def buf = s.toCharArray()
        int i = 0, j = s.length() - 1
        while (i < j) {
            while (i < j && !isv[(int)buf[i]]) {
                ++i
            }
            while (i < j && !isv[(int)buf[j]]) {
                --j
            }
            if (i < j) {
                def temp = buf[i]
                buf[i] = buf[j]
                buf[j] = temp
                ++i
                --j
            }
        }
        new String(buf)
    }
}

def work = TestWork.create(new Solution().&reverseVowels)
// work.validator = { i, j -> i == j }
println work.run(System.in.getText('UTF-8'))
