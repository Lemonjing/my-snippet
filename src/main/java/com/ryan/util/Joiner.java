package com.ryan.util;

import java.util.Arrays;

/**
 * Joins multiple {@code Strings} into a single one. Instances of this class are
 * immutable.
 *
 * @author Osman KOCAK
 * @see Splitter
 */
public final class Joiner {
    /**
     * Returns a new {@code Joiner} that will use the specified separator,
     * no prefix, no suffix and {@code "null"} for {@code null} values.
     *
     * @param separator the separator.
     * @return the created {@code Joiner}.
     * @throws NullPointerException if {@code separator} is {@code null}.
     */
    public static Joiner on(String separator) {
        return new Joiner(separator, "", "", false, false, false,
                "null", "");
    }

    private final String separator;
    private final String prefix;
    private final String suffix;
    private final boolean trim;
    private final boolean ignoreNull;
    private final boolean ignoreEmpty;
    private final String forNull;
    private final String forEmpty;

    private Joiner(String separator, String prefix, String suffix,
                   boolean trim, boolean ignoreNull, boolean ignoreEmpty,
                   String forNull, String forEmpty) {
        Parameters.checkNotNull(separator);
        Parameters.checkNotNull(prefix);
        Parameters.checkNotNull(suffix);
        Parameters.checkNotNull(forNull);
        Parameters.checkNotNull(forEmpty);
        this.separator = separator;
        this.prefix = prefix;
        this.suffix = suffix;
        this.trim = trim;
        this.ignoreNull = ignoreNull;
        this.ignoreEmpty = ignoreEmpty;
        this.forNull = forNull;
        this.forEmpty = forEmpty;
    }

    /**
     * Returns a new {@code Joiner} that will behave as this one except that
     * it will trim inputs before joining them.
     *
     * @return a new {@code Joiner} with the desired configuration.
     */
    public Joiner trimInputs() {
        return new Joiner(separator, prefix, suffix, true, ignoreNull,
                ignoreEmpty, forNull, forEmpty);
    }

    /**
     * Returns a new {@code Joiner} that will behave as this one except that
     * it will ignore {@code null} input values.
     *
     * @return a new {@code Joiner} with the desired configuration.
     */
    public Joiner ignoreNulls() {
        return new Joiner(separator, prefix, suffix, trim, true,
                ignoreEmpty, forNull, forEmpty);
    }

    /**
     * Returns a new {@code Joiner} that will behave as this one except that
     * it will ignore empty input {@code String}s.
     *
     * @return a new {@code Joiner} with the desired configuration.
     */
    public Joiner ignoreEmptyStrings() {
        return new Joiner(separator, prefix, suffix, trim, ignoreNull,
                true, forNull, forEmpty);
    }

    /**
     * Returns a new {@code Joiner} that will behave as this one except that
     * it will replace {@code null} input values with the specified
     * {@code String}.
     *
     * @param forNull the {@code String} to use for {@code null}s.
     * @return a new {@code Joiner} with the desired configuration.
     * @throws NullPointerException if {@code forNull} is {@code null}.
     */
    public Joiner replaceNullWith(String forNull) {
        return new Joiner(separator, prefix, suffix, trim, false,
                ignoreEmpty, forNull, forEmpty);
    }

    /**
     * Returns a new {@code Joiner} that will behave as this one except that
     * it will replace empty input {@code String}s with the specified value.
     *
     * @param forEmpty the value to use for empty {@code String}s.
     * @return a new {@code Joiner} with the desired configuration.
     * @throws NullPointerException if {@code forEmpty} is {@code null}.
     */
    public Joiner replaceEmptyStringWith(String forEmpty) {
        return new Joiner(separator, prefix, suffix, trim, ignoreNull,
                false, forNull, forEmpty);
    }

    /**
     * Returns a new {@code Joiner} that will behave as this one except that
     * it will use the specified prefix when joining {@code String}s.
     *
     * @param prefix the prefix.
     * @return a new {@code Joiner} with the desired configuration.
     * @throws NullPointerException if {@code prefix} is {@code null}.
     */
    public Joiner withPrefix(String prefix) {
        return new Joiner(separator, prefix, suffix,
                trim, ignoreNull, ignoreEmpty, forNull, forEmpty);
    }

    /**
     * Returns a new {@code Joiner} that will behave as this one except that
     * it will use the specified suffix when joining {@code String}s.
     *
     * @param suffix the suffix.
     * @return a new {@code Joiner} with the desired configuration.
     * @throws NullPointerException if {@code suffix} is {@code null}.
     */
    public Joiner withSuffix(String suffix) {
        return new Joiner(separator, prefix, suffix,
                trim, ignoreNull, ignoreEmpty, forNull, forEmpty);
    }

    /**
     * Joins the given {@code String}s according to its configuration.
     *
     * @param parts the parts to join.
     * @return the created {@code String}.
     * @throws NullPointerException if {@code parts} is {@code null}.
     */
    public String join(Object... parts) {
        return join(Arrays.asList(parts));
    }

    /**
     * Joins the given {@code String}s according to its configuration.
     *
     * @param parts the parts to join.
     * @return the created {@code String}.
     * @throws NullPointerException if {@code parts} is {@code null}.
     */
    public String join(Iterable<?> parts) {
        boolean first = true;
        StringBuilder sb = new StringBuilder(prefix);
        for (Object part : parts) {
            String s = format(part);
            if (s != null) {
                if (first) {
                    first = false;
                } else {
                    sb.append(separator);
                }
                sb.append(s);
            }
        }
        return sb.append(suffix).toString();
    }

    private String format(Object o) {
        if (ignoreNull && o == null) {
            return null;
        }
        String s = XObjects.toString(o, forNull);
        s = trim ? s.trim() : s;
        return s.isEmpty() ? (ignoreEmpty ? null : forEmpty) : s;
    }
}
