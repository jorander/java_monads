package com.enelson.monads.algebra;

import javaslang.collection.List;

public class ListMonoid<T> implements Monoid<List> {

    public static <T> ListMonoid of(List<T> list) {
        return new ListMonoid(list);
    }

    public static <T> ListMonoid of(T t) {
        return new ListMonoid(List.of(t));
    }

    private List<T> list;

    private ListMonoid(List<T> list) {
        this.list = list;
    }

    @Override
    public List<T> zero() {
        return List.empty();
    }

    @Override
    public Monoid<List> append(Monoid<List> monoid) {
        return new ListMonoid(list.appendAll(((ListMonoid)monoid).getList()));
    }

    @Override
    public String getDelimiter() {
        return null;
    }

    public List<T> getList() {
        return list;
    }

    @Override
    public String toString() {
        return list.toString();
    }

}
