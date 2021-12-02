package soda.unittest.job.codec;

import java.util.ArrayList;
import java.util.List;

public class CharArray2dCodec implements ICodec<List<List<String>>, char[][]> {

    private CharArrayCodec c = new CharArrayCodec();

    @Override
    public List<List<String>> encode(char[][] object) {
        List<List<String>> res = new ArrayList<>();
        for (char[] chars : object) {
            res.add(c.encode(chars));
        }
        return res;
    }

    @Override
    public char[][] decode(List<List<String>> lists) {
        char[][] res = new char[lists.size()][];
        for (int i = 0; i < res.length; ++i) {
            res[i] = c.decode(lists.get(i));
        }
        return res;
    }
}
