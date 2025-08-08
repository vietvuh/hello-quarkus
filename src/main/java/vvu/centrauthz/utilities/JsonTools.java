package vvu.centrauthz.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import vvu.centrauthz.errors.IllegalJsonValue;

import java.util.function.Supplier;

public class JsonTools {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    JsonTools() {
        throw new IllegalStateException();
    }

    static <T> T jsonContext(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new IllegalJsonValue(e);
        }
    }

    public static ObjectMapper mapper() {
        return MAPPER;
    }
}
