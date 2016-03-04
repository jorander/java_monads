package com.enelson.monads;

import javaslang.Tuple2;
import javaslang.collection.List;
import javaslang.collection.Seq;

import java.util.function.Function;

/**
 * A monad type that wraps a unary function, passing the context
 * along to other chained (flatMapped) functions.
 *
 * @author Eric Nelson
 * @since 1.0
 */
public class Reader<CTX, T> {

    private Function<CTX, T> runner;

    private Reader(Function<CTX, T> runner) {
        this.runner = runner;
    }

    public static <CTX, T> Reader<CTX, T> of(Function<CTX, T> f) {
        return new Reader<>(f);
    }

    public static <CTX, T> Reader<CTX, T> pure(T t) {
        return new Reader<>((CTX ctx) -> t);
    }

    public static <CTX, T> Reader<CTX, ? extends Seq<T>> sequence(Seq<Reader<CTX, T>> readers) {
        return new Reader<>((CTX ctx) -> {
            List<T> list = List.empty();
            for(Reader<CTX, T> r : readers) {
                list = list.append(r.apply(ctx));
            }
            return list;
        });
    }

    public T apply(CTX ctx) {
        return runner.apply(ctx);
    }

    public <U> Reader<CTX, U> map(Function<? super T, ? extends U> f) {
        return new Reader<>((CTX ctx) -> f.apply(apply(ctx)));
    }

    public <U> Reader<CTX, U> flatMap(Function<? super T, Reader<CTX, ? extends U>> f) {
        return new Reader<>((CTX ctx) -> f.apply(apply(ctx)).apply(ctx));
    }

    public <U> Reader<CTX, Tuple2<T, U>> zip(Reader<CTX, U> reader) {
        return this.flatMap(a -> reader.map(b -> new Tuple2<>(a, b)));
    }

}
