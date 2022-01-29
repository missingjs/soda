package soda.unittest;

import soda.unittest.conv.ConverterFactory;
import soda.unittest.conv.ObjectConverter;
import soda.unittest.validate.ValidatorFactory;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.function.BiPredicate;

class Validatable<R> {

    public boolean compareSerial;

    public BiPredicate<R,R> validator;

    public R expectedOutput;

    public void validate(WorkInput input, Type retType, R result, WorkOutput output) {
        Utils.wrapEx(() -> {
            ObjectConverter<R, Object> resConv = Utils.cast(ConverterFactory.createConverter(retType));
            output.result = resConv.toJsonSerializable(result);
            boolean success = true;
            if (input.expected != null) {
                if (compareSerial && validator == null) {
                    String a = Utils.objectMapper.writeValueAsString(input.expected);
                    String b = Utils.objectMapper.writeValueAsString(output.result);
                    success = Objects.equals(a, b);
                } else {
                    var expect = resConv.fromJsonSerializable(input.expected);
                    expectedOutput = expect;
                    if (validator != null) {
                        success = validator.test(expect, result);
                    } else {
                        success = ValidatorFactory.create(retType).test(expect, result);
                    }
                }
            }
            output.success = success;
        });
    }

}
