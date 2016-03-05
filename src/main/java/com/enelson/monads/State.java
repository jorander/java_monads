package com.enelson.monads;

import javaslang.Tuple2;

import java.util.function.Function;

/**
 * Created by enelson on 3/4/16.
 */
public class State<S, A> {

    private Function<S, Tuple2<S,A>> runner;

    private State(Function<S, Tuple2<S,A>> runner) {
        this.runner = runner;
    }

    public static <S, A> State<S, A> of(Function<S, Tuple2<S,A>> f) {
        return new State<>(f);
    }

    public static <S> State<S, S> get() {
        return new State<>(s -> new Tuple2<>(s, s));
    }

    public static <S, A> State<S, A> gets(Function<S, A> f) {
        return new State<>(s -> new Tuple2<>(s, f.apply(s)));
    }

    public static <S, A> State<S, A> put(S sNew) {
        return new State<>(s -> new Tuple2<>(sNew, null));
    }

    public static <S, A> State<S, A> modify(Function<S, S> f) {
        return new State<>(s -> new Tuple2<>(f.apply(s), null));
    }

    public Tuple2<S, A> apply(S s) {
        return runner.apply(s);
    }

    public <B> State<S, B> map(Function<? super A, ? extends B> f) {
        return new State<>(s -> {
            Tuple2<S, A> value = runner.apply(s);
            return new Tuple2<>(s, f.apply(value._2));
        });
    }

    public <B> State<S, B> flatMap(Function<A, State<S, B>> f) {
        return new State<>(s -> {
            Tuple2<S, A> value = runner.apply(s);
            return f.apply(value._2).apply(s);
        });
    }

    @Override
    public String toString() {
        return "State";
    }

}
