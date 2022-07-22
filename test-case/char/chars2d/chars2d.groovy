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

class Chars2dWork {
    String call(String input) {
        def work = TestWork.create(new Solution().&toUpper)
        // def work = TestWork.forStruct(STRUCT)
        // work.validator = { i, j -> i == j }
        work.compareSerial = true
        work.run(input)
    }
}

println new Chars2dWork()(System.in.getText('UTF-8'))
