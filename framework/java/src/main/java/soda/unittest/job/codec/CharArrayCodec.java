package soda.unittest.job.codec;

import java.util.ArrayList;
import java.util.List;

public class CharArrayCodec implements ICodec<List<String>, char[]> {

    @Override
    public List<String> encode(char[] object) {
        char[] buf = new char[1];
        List<String> res = new ArrayList<>();
        for (char ch : object) {
            buf[0] = ch;
            res.add(new String(buf));
        }
        return res;
    }

    @Override
    public char[] decode(List<String> strings) {
        char[] res = new char[strings.size()];
        for (int i = 0; i < strings.size(); ++i) {
            res[i] = strings.get(i).charAt(0);
        }
        return res;
    }
}
