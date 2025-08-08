package vvu.centrauthz.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import vvu.centrauthz.models.User;

import java.util.function.Function;

@Builder(toBuilder = true)
public record Context(User user, String token, JsonNode attributes) {
    public static Context empty() {
        return Context.builder().build();
    }

    public static Context of(User user) {
        return Context.builder().user(user).build();
    }

    public static Context of(String userId) {
        User user = User.builder().userId(userId).build();
        return Context.builder().user(user).build();
    }

    public <T> T execute(Function<Context, T> function) {
        return function.apply(this);
    }
}
