package com.enelson.monads;

import com.enelson.monads.algebra.Monoid;
import javaslang.Tuple2;

import java.util.function.Function;

/**
 * Writer monad allows a log to be passed through chained calls.
 *
 * @author Eric Nelson
 * @since 1.0
 */
public class Writer<W extends Monoid, A> {

    private W logs;
    private A value;

    private Writer(W logs, A value) {
        this.logs = logs;
        this.value = value;
    }

    public static <W extends Monoid, A> Writer<W, A> of(W log, A value) {
        return new Writer<>(log, value);
    }

    public <B> Writer<W, B> map(Function<A, B> f) {
        return new Writer<>(logs, f.apply(value));
    }

    public <B> Writer<W, B> flatMap(Function<A, Writer<W, B>> f) {
        Writer<W, B> mappedWriter = f.apply(value);
        return new Writer<>((W) logs.append(mappedWriter.logs), mappedWriter.value);
    }

    public Tuple2<W, A> getContext() {
        return new Tuple2<>(logs, value);
    }

    public W getLogs() {
        return logs;
    }

    public A getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Writer("+logs+", "+value+")";
    }

}
