import soda.groovy.unittest.*

class Solution {
    public char[][] toUpper(List<List<Character>> matrix) {
        def diff = ((char) 'a') - ((char) 'A')
        char[][] mx = new char[matrix.size()][matrix[0].size()]
        for (int i = 0; i < matrix.size(); ++i) {
            for (int j = 0; j < matrix[0].size(); ++j) {
                mx[i][j] = (char) (matrix[i][j] - diff)
            }
        }
        mx
    }
}

def work = TestWork.create(new Solution().&toUpper)
// work.setValidator { i, j -> i == j }
println work.run(System.in.getText('UTF-8'))
