package com.enelson.monads;

import javaslang.Tuple2;
import javaslang.collection.List;

import java.util.function.Function;

/**
 * Writer monad allows a log to be passed through chained calls.
 *
 * @author Eric Nelson
 * @since 1.0
 */
public class Writer<A> {

    private List<String> logs;
    private A value;

    private Writer(List<String> logs, A value) {
        this.logs = logs;
        this.value = value;
    }

    public static <A> Writer<A> of(String log, A value) {
        return new Writer<>(List.of(log), value);
    }

    public <B> Writer<B> map(Function<A, B> f) {
        return new Writer<>(logs, f.apply(value));
    }

    public <B> Writer<B> flatMap(Function<A, Writer<B>> f) {
        Writer<B> mappedWriter = f.apply(value);
        return new Writer<>(logs.appendAll(mappedWriter.logs), mappedWriter.value);
    }

    public Tuple2<List<String>, A> getContext() {
        return new Tuple2<>(logs, value);
    }

    public List<String> getLogs() {
        return logs;
    }

    public A getValue() {
        return value;
    }

    @Override
    public String toString() {
        String logsString = logs.mkString(", ");
        return "Writer("+logsString+", "+value+")";
    }
}
