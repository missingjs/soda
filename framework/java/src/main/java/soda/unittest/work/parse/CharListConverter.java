package soda.unittest.work.parse;

import java.util.List;
import java.util.stream.Collectors;

public class CharListConverter implements ObjectConverter<List<Character>, List<String>> {
    @Override
    public List<Character> fromJsonSerializable(List<String> strs) {
        return strs.stream().map(s -> s.charAt(0)).collect(Collectors.toList());
    }

    @Override
    public List<String> toJsonSerializable(List<Character> chs) {
        return chs.stream().map(c -> "" + c).collect(Collectors.toList());
    }
}
