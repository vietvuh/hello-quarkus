package vvu.centrauthz.models;

public record Void() {
    public static Void of() {
        return new Void();
    }

    public static Void INSTANCE = of();
}
