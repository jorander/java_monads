package com.enelson.monads;

import javaslang.Tuple2;
import javaslang.collection.List;

import java.util.function.Function;

/**
 * A monad type that wraps a unary function, passing the context
 * along to other chained (flatMapped) functions.
 *
 * @author Eric Nelson
 * @since 1.0
 */
public class Reader<CTX, A> {

    private Function<CTX, A> runner;

    private Reader(Function<CTX, A> runner) {
        this.runner = runner;
    }

    public static <CTX, A> Reader<CTX, A> of(Function<CTX, A> f) {
        return new Reader<>(f);
    }

    public static <CTX, A> Reader<CTX, A> pure(A a) {
        return new Reader<>(ctx -> a);
    }

    public static <CTX, A> Reader<CTX, List<A>> sequence(Iterable<Reader<CTX, A>> readers) {
        return new Reader<>(ctx -> {
            List<A> list = List.empty();
            for(Reader<CTX, A> r : readers) {
                list = list.append(r.apply(ctx));
            }
            return list;
        });
    }

    public A apply(CTX ctx) {
        return runner.apply(ctx);
    }

    public <U> Reader<CTX, U> map(Function<? super A, ? extends U> f) {
        return new Reader<>(ctx -> f.apply(apply(ctx)));
    }

    public <U> Reader<CTX, U> flatMap(Function<? super A, Reader<CTX, ? extends U>> f) {
        return new Reader<>(ctx -> f.apply(apply(ctx)).apply(ctx));
    }

    public <U> Reader<CTX, Tuple2<A, U>> zip(Reader<CTX, U> reader) {
        return this.flatMap(a -> reader.map(b -> new Tuple2<>(a, b)));
    }

}
