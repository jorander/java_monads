package com.enelson.monads.algebra;

public interface Monoid<A> {

    A zero();
    Monoid<A> append(Monoid<A> monoid);
    String getDelimiter();

}
