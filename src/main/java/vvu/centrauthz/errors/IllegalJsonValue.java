package vvu.centrauthz.errors;
import vvu.centrauthz.models.Error;

public class IllegalJsonValue extends AppError {
    public IllegalJsonValue(Error e) {
        super(e);
    }

    public IllegalJsonValue(String message) {
        super("ILLEGAL_JSON_VALUE", message);
    }

    public IllegalJsonValue(Throwable e) {
        super("ILLEGAL_JSON_VALUE", e);
    }
}
