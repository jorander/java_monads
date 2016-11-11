package com.enelson.monads;

import javaslang.control.Option;
import javaslang.control.Try;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This type is to faciliate easier mapping/flatMapping for the multi-layered Monad structure {@code Try<Option<T>>}.
 * Instead of having to do 2-layered map/flatMap, you only have to call it once, and this structure knows how
 * to dive into the underlying value.
 *
 * @param <T> The type of the wrapped value
 */
public class TryOptionT<T> {

    private Try<Option<T>> t;

    private TryOptionT(Try<Option<T>> t) {
        this.t = t;
    }

    /**
     * Creates a {@code TryOptionT} from the provided T. If this value is null, it will convert it into an {@code Option.None}
     * @param t An instance of T
     * @param <T> The type of the wrapped value
     * @return An instance of {@code TryOptionT<T>}
     */
    public static <T> TryOptionT<T> of(T t) {
        if(t != null) {
            return new TryOptionT<>(Try.success(Option.some(t)));
        } else {
            return new TryOptionT<>(Try.success(Option.none()));
        }
    }

    /**
     * Creates a {@code TryOptionT} from the result of the {@link Supplier}. If the returned value is null, it will convert it into an {@code Option.None}
     * If the supplier throws an {@link Exception}, it will be wrapped in a {@code Try.Failure}
     * @param s A supplier which return a T
     * @param <T> The type of the wrapped value
     * @return An instance of {@code TryOptionT<T>}
     */
    public static <T> TryOptionT<T> of(Supplier<T> s) {
        try {
            T t = s.get();
            return of(t);
        } catch (Exception ex) {
            return new TryOptionT<>(Try.failure(ex));
        }
    }

    /**
     * Lifts an existing value of type {@code Try<Option<T>>} into the context of a {@code TryOptionT}
     * @param t An instance of T
     * @param <T> The type of the wrapped value
     * @return An instance of {@code TryOptionT<T>}
     */
    public static <T> TryOptionT<T> lift(Try<Option<T>> t) {
        return new TryOptionT<>(t);
    }

    /**
     * Converts an instance of {@code TryOptionT<T>} into an instance of U. If this {@code TryOptionT} instance is
     * of type {@code Try.Failure}, run the failure() method.
     * @param failure Function that takes in the wrapped Exception and maps it to a value of type U
     * @param success Function that takes in the wrapped value and maps it to a value of type U
     * @param <U> The type of the value returned by 'success' or 'failure'
     * @return
     */
    public <U> U fold(Function<Throwable, U> failure, Function<T, U> success) {
        if(t.isFailure()) {
            return failure.apply(t.getCause());
        } else {
            Option<T> option = t.get();
            if(option.isDefined()) {
                return success.apply(option.get());
            } else {
                return null;
            }
        }
    }

    /**
     * Filters the wrapped value T with the predicate 'p'
     * @param p The {@link Predicate} which filters on the value T
     * @return An instance of {@code TryOptionT} with the value T if it passes the filter, or {@code Option.None} if it doesn't
     */
    public TryOptionT<T> filter(Predicate<? super T> p) {
        if(t.isSuccess()) {
            Option<T> option = t.get();
            return new TryOptionT<>(Try.success(option.filter(p)));
        } else {
            return this;
        }
    }

    /**
     * Maps the underlying value T to a U
     * @param f Function to convert a T to a U
     * @param <U> The type of the value returned by 'f'
     * @return An instance of {@code TryOptionT<U>}
     */
    public <U> TryOptionT<U> map(Function<? super T, ? extends U> f) {
        return new TryOptionT<>(
                t.map(x -> x.map(f))
        );
    }

    /**
     * Composes this instance of {@code TryOptionT<T>} with a function returning another {@code TryOptionT<U>}
     * @param f Function that takes in a {@code T} and returns a {@code TryOptionT<U>}
     * @param <U> The wrapped type of the value returned by 'f'
     * @return An instance of {@code TryOptionT<U>}
     */
    public <U> TryOptionT<U> flatMap(Function<? super T, TryOptionT<? extends U>> f) {
        if(t.isSuccess()) {
            Option<T> option = t.get();
            if(option.isDefined()) {
                try {
                    return (TryOptionT<U>) f.apply(option.get());
                } catch(Exception ex) {
                    return new TryOptionT<>(Try.failure(ex));
                }
            } else {
                return (TryOptionT<U>) this;
            }
        } else {
            return (TryOptionT<U>) this;
        }
    }

    /**
     * Composes this instance of {@code TryOptionT<T>} with a function returning a {@code Try<U>}. The result will
     * be properly wrapped inside of an {@code Option}
     * @param f Function that takes in a {@code T} and return a {@code Try<U>}
     * @param <U> The wrapped type of the value returned by 'f'
     * @return An instance of {@code TryOptionT<U>}
     */
    public <U> TryOptionT<U> flatMapT(Function<? super T, Try<? extends U>> f) {
        if(t.isSuccess()) {
            Option<T> option = t.get();
            if(option.isDefined()) {
                Try<U> newTry = null;
                try {
                    newTry = (Try<U>) f.apply(option.get());
                    return new TryOptionT<>(newTry.map(u -> Option.of(u)));
                } catch (Exception e) {
                    return new TryOptionT<>(Try.failure(e));
                }
            } else {
                return (TryOptionT<U>) this;
            }
        } else {
            return (TryOptionT<U>) this;
        }
    }

    public <U> TryOptionT<U> flatMapO(Function<? super T, Option<? extends U>> f) {
        if(t.isSuccess()) {
            Option<T> option = t.get();
            if(option.isDefined()) {
                Option<U> newOption = null;
                try {
                    newOption = (Option<U>) f.apply(option.get());
                    return new TryOptionT<>(Try.success(newOption));
                } catch (Exception e) {
                    return new TryOptionT<>(Try.failure(e));
                }
            } else {
                return (TryOptionT<U>) this;
            }
        } else {
            return (TryOptionT<U>) this;
        }
    }

    /**
     * Extracts the underlying {@code Try<Option<T>>} to take it out of the {@code TryOptionT} context
     * @return The underlying {@code Try<Option<T>>}
     */
    public Try<Option<T>> run() {
        return t;
    }

}