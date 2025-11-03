package org.example.domain.model;

import java.util.Objects;
import java.util.regex.Pattern;
public class AccountNumber {
    private static final Pattern PATTERN = Pattern.compile("^\\d{10}$");
    private final String value;

    public AccountNumber(String value) {
        if (value == null || !PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Conta deve ter 10 dígitos (XXXXXXXXXX).");
        }
        this.value = value;
    }
    public String value() { return value; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountNumber)) return false;
        AccountNumber that = (AccountNumber) o;
        return value.equals(that.value);
    }
    @Override public int hashCode() { return Objects.hash(value); }
    @Override public String toString() { return value; }
}
