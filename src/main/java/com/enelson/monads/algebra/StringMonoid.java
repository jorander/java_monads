package com.enelson.monads.algebra;

public class StringMonoid implements Monoid<String> {

    public static StringMonoid of(String s) {
        return new StringMonoid(s);
    }

    private String str;

    public StringMonoid(String str) {
        this.str = str;
    }

    @Override
    public String zero() {
        return "";
    }

    @Override
    public Monoid<String> append(Monoid<String> monoid) {
        String s = ((StringMonoid) monoid).getStr();
        return new StringMonoid(str + getDelimiter() + s);
    }

    @Override
    public String getDelimiter() {
        return ",";
    }

    public String getStr() {
        return str;
    }
}
