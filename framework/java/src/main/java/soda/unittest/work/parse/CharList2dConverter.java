package soda.unittest.work.parse;

import java.util.List;
import java.util.stream.Collectors;

public class CharList2dConverter implements ObjectConverter<List<List<Character>>, List<List<String>>> {

    private CharListConverter conv = new CharListConverter();

    @Override
    public List<List<Character>> fromJsonSerializable(List<List<String>> lists) {
        return lists.stream().map(s -> conv.fromJsonSerializable(s)).collect(Collectors.toList());
    }

    @Override
    public List<List<String>> toJsonSerializable(List<List<Character>> lists) {
        return lists.stream().map(c -> conv.toJsonSerializable(c)).collect(Collectors.toList());
    }
}
