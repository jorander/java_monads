package com.enelson.monads;

import javaslang.control.Option;
import javaslang.control.Try;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TryOptionTUnitTest {

    private static final String TEST_STRING = "Test";
    private static final String EXCEPTION_MESSAGE = "Test Exception";
    private static final RuntimeException EXCEPTION = new RuntimeException(EXCEPTION_MESSAGE);

    @Test
    public void testOfT() {
        TryOptionT<String> t  = TryOptionT.of(TEST_STRING);

        assertThat(t).isNotNull();
        assertThat(t.run().isSuccess()).isTrue();
        assertThat(t.run().get().isDefined()).isTrue();
        assertThat(t.run().get().get()).isEqualTo(TEST_STRING);
    }

    @Test
    public void testOfTNull() {
        TryOptionT<String> t1  = TryOptionT.of((String) null);
        TryOptionT<String> t2 = TryOptionT.of(() -> {
            if(true)
                throw EXCEPTION;

            return "";
        });

        assertThat(t1).isNotNull();
        assertThat(t1.run().isSuccess()).isTrue();
        assertThat(t1.run().get().isDefined()).isFalse();

        assertThat(t2).isNotNull();
        assertThat(t2.run().isFailure()).isTrue();
        assertThat(t2.run().getCause()).hasMessage(EXCEPTION_MESSAGE);
    }

    @Test
    public void testOfSupplierT() {
        TryOptionT<String> t1 = TryOptionT.of(() -> TEST_STRING);
        TryOptionT<String> t2 = TryOptionT.of(this::getString);

        assertThat(t1).isNotNull();
        assertThat(t1.run().isSuccess()).isTrue();
        assertThat(t1.run().get().isDefined()).isTrue();
        assertThat(t1.run().get().get()).isEqualTo(TEST_STRING);

        assertThat(t2).isNotNull();
        assertThat(t2.run().isSuccess()).isTrue();
        assertThat(t2.run().get().isDefined()).isTrue();
        assertThat(t2.run().get().get()).isEqualTo(TEST_STRING);
    }

    @Test
    public void testLift() {
        Try<Option<String>> to = Try.success(Option.some(TEST_STRING));
        TryOptionT<String> t = TryOptionT.lift(to);

        assertThat(t).isNotNull();
        assertThat(t.run().isSuccess()).isTrue();
        assertThat(t.run().get().isDefined()).isTrue();
        assertThat(t.run().get().get()).isEqualTo(TEST_STRING);
    }

    @Test
    public void testFold() {
        TryOptionT<String> t1 = TryOptionT.of(TEST_STRING);
        String result = t1.fold(
                Throwable::getMessage,
                success -> success+"!"
        );

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(TEST_STRING+"!");
    }

    @Test
    public void testFoldFailure() {
        TryOptionT<String> t1 = TryOptionT.of(() -> {
            throw EXCEPTION;
        });
        String result = t1.fold(
                Throwable::getMessage,
                success -> success
        );

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(EXCEPTION_MESSAGE);
    }

    @Test
    public void testFoldNull() {
        TryOptionT<String> t1 = TryOptionT.of((String) null);
        String result = t1.fold(
                Throwable::getMessage,
                success -> success+"!"
        );

        assertThat(result).isNull();
    }

    @Test
    public void testFilter() {
        TryOptionT<String> t1 = TryOptionT.of(TEST_STRING);
        TryOptionT<String> t2 = t1.filter(s -> s.length() > 1);

        assertThat(t2).isNotNull();
        assertThat(t2.run().isSuccess()).isTrue();
        assertThat(t2.run().get().isDefined()).isTrue();
        assertThat(t2.run().get().get()).isEqualTo(TEST_STRING);
    }

    @Test
    public void testFilterFailure() {
        TryOptionT<String> t1 = TryOptionT.of(() -> {
            throw EXCEPTION;
        });
        TryOptionT<String> t2 = t1.filter(s -> s.length() > 1);

        assertThat(t2).isNotNull();
        assertThat(t2.run().isFailure()).isTrue();
        assertThat(t2.run().getCause()).hasMessage(EXCEPTION_MESSAGE);
    }

    @Test
    public void testFilterNull() {
        TryOptionT<String> t1 = TryOptionT.of((String) null);
        TryOptionT<String> t2 = t1.filter(s -> s.length() > 1);

        assertThat(t2).isNotNull();
        assertThat(t2.run().isSuccess()).isTrue();
        assertThat(t2.run().get().isDefined()).isFalse();
    }

    @Test
    public void testMap() {
        TryOptionT<String>  t1  = TryOptionT.of("1");
        TryOptionT<Integer> t2  = t1.map(s -> Integer.valueOf(s));

        assertThat(t2).isNotNull();
        assertThat(t2.run().isSuccess()).isTrue();
        assertThat(t2.run().get().isDefined()).isTrue();
        assertThat(t2.run().get().get()).isEqualTo(1);
    }

    @Test
    public void testMapNone() {
        TryOptionT<String>  t1  = TryOptionT.of((String) null);
        TryOptionT<Integer> t2  = t1.map(s -> Integer.valueOf(s));

        assertThat(t2).isNotNull();
        assertThat(t2.run().isSuccess()).isTrue();
        assertThat(t2.run().get().isDefined()).isFalse();
    }

    @Test
    public void testMapFailure() {
        TryOptionT<String>  t1  = TryOptionT.of(() -> {
            throw EXCEPTION;
        });
        TryOptionT<Integer> t2  = t1.map(s -> Integer.valueOf(s));

        assertThat(t2).isNotNull();
        assertThat(t2.run().isFailure()).isTrue();
        assertThat(t2.run().getCause()).hasMessage(EXCEPTION_MESSAGE);
    }

    @Test
    public void testMapFailure2() {
        TryOptionT<String> t1 = TryOptionT.of("1");
        TryOptionT<String> t2 = t1.map(s -> {
            if(true)
                throw EXCEPTION;

            return "2";
        });

        assertThat(t2).isNotNull();
        assertThat(t2.run().isFailure()).isTrue();
        assertThat(t2.run().getCause()).hasMessage(EXCEPTION_MESSAGE);
    }

    @Test
    public void testFlatMap() {
        TryOptionT<String>  t1  = TryOptionT.of("1");
        TryOptionT<Integer> t2  = t1.flatMap(this::convertStringToInt);
        TryOptionT<Integer> t3  = t1.flatMap(s -> TryOptionT.of(Integer.valueOf(s)));

        assertThat(t2).isNotNull();
        assertThat(t2.run().isSuccess()).isTrue();
        assertThat(t2.run().get().isDefined()).isTrue();
        assertThat(t2.run().get().get()).isEqualTo(1);

        assertThat(t3).isNotNull();
        assertThat(t3.run().isSuccess()).isTrue();
        assertThat(t3.run().get().isDefined()).isTrue();
        assertThat(t3.run().get().get()).isEqualTo(1);
    }

    @Test
    public void testFlatMapNone() {
        TryOptionT<String>  t1  = TryOptionT.of((String) null);
        TryOptionT<Integer> t2  = t1.flatMap(this::convertStringToInt);

        assertThat(t2).isNotNull();
        assertThat(t2.run().isSuccess()).isTrue();
        assertThat(t2.run().get().isDefined()).isFalse();
    }

    @Test
    public void testFlatMapFailure() {
        TryOptionT<String>  t1  = TryOptionT.lift(Try.failure(EXCEPTION));
        TryOptionT<Integer> t2  = t1.flatMap(this::convertStringToInt);

        assertThat(t2).isNotNull();
        assertThat(t2.run().isFailure()).isTrue();
        assertThat(t2.run().getCause()).hasMessage(EXCEPTION_MESSAGE);

        t1  = TryOptionT.of("x");
        t2  = t1.flatMap(this::convertStringToInt);

        assertThat(t2).isNotNull();
        assertThat(t2.run().isFailure()).isTrue();
        assertThat(t2.run().getCause()).hasMessageContaining("For input string:");
    }

    @Test
    public void testFlatMapException() {
        TryOptionT<String> t1 = TryOptionT.of("1");
        TryOptionT<String> t2 = t1.flatMap(s -> {
            if(true)
                throw EXCEPTION;
            return TryOptionT.of("2");
        });

        assertThat(t2).isNotNull();
        assertThat(t2.run().isFailure()).isTrue();
        assertThat(t2.run().getCause()).hasMessage(EXCEPTION_MESSAGE);
    }

    @Test
    public void testFlatMapT() {
        TryOptionT<String>  t1 = TryOptionT.of("1");
        TryOptionT<Integer> t2 = t1.flatMapT(this::convertStringToIntT);

        assertThat(t2).isNotNull();
        assertThat(t2.run().isSuccess()).isTrue();
        assertThat(t2.run().get().isDefined()).isTrue();
        assertThat(t2.run().get().get()).isEqualTo(1);
    }

    @Test
    public void testFlatMapTNone() {
        TryOptionT<String>  t1  = TryOptionT.of((String) null);
        TryOptionT<Integer> t2  = t1.flatMapT(this::convertStringToIntT);

        assertThat(t2).isNotNull();
        assertThat(t2.run().isSuccess()).isTrue();
        assertThat(t2.run().get().isDefined()).isFalse();
    }

    @Test
    public void testFlatMapTFailure() {
        TryOptionT<String>  t1  = TryOptionT.lift(Try.failure(EXCEPTION));
        TryOptionT<Integer> t2  = t1.flatMapT(this::convertStringToIntT);

        assertThat(t2).isNotNull();
        assertThat(t2.run().isFailure()).isTrue();
        assertThat(t2.run().getCause()).hasMessage(EXCEPTION_MESSAGE);

        t1  = TryOptionT.of("x");
        t2  = t1.flatMapT(this::convertStringToIntT);

        assertThat(t2).isNotNull();
        assertThat(t2.run().isFailure()).isTrue();
        assertThat(t2.run().getCause()).hasMessageContaining("For input string:");

        t1  = TryOptionT.of("x");
        t2  = t1.flatMapT(s -> {
            if(true)
                throw EXCEPTION;

            return convertStringToIntT(s);
        });

        assertThat(t2).isNotNull();
        assertThat(t2.run().isFailure()).isTrue();
        assertThat(t2.run().getCause()).hasMessage(EXCEPTION_MESSAGE);
    }

    @Test
    public void testFlatMapO() {
        TryOptionT<String>  t1 = TryOptionT.of("1");
        TryOptionT<Integer> t2 = t1.flatMapO(this::convertStringtoIntO);

        assertThat(t2).isNotNull();
        assertThat(t2.run().isSuccess()).isTrue();
        assertThat(t2.run().get().isDefined()).isTrue();
        assertThat(t2.run().get().get()).isEqualTo(1);
    }

    @Test
    public void testFlatMapONone() {
        TryOptionT<String>  t1 = TryOptionT.of((String) null);
        TryOptionT<Integer> t2 = t1.flatMapO(this::convertStringtoIntO);

        assertThat(t2).isNotNull();
        assertThat(t2.run().isSuccess()).isTrue();
        assertThat(t2.run().get().isDefined()).isFalse();
    }

    @Test
    public void testFlatMapOFailure() {
        TryOptionT<String>  t1 = TryOptionT.lift(Try.failure(EXCEPTION));
        TryOptionT<Integer> t2 = t1.flatMapO(this::convertStringtoIntO);

        assertThat(t2).isNotNull();
        assertThat(t2.run().isFailure()).isTrue();
        assertThat(t2.run().getCause()).hasMessage(EXCEPTION_MESSAGE);

        t1 = TryOptionT.of("x");
        t2 = t1.flatMapO(this::convertStringtoIntO);

        assertThat(t2).isNotNull();
        assertThat(t2.run().isFailure()).isTrue();
        assertThat(t2.run().getCause()).hasMessageContaining("For input string:");
    }

    @Test
    public void restRun() {
        TryOptionT<String> t  = TryOptionT.of(TEST_STRING);
        Try<Option<String>> to = t.run();

        assertThat(to).isNotNull();
        assertThat(to.isSuccess()).isTrue();
        assertThat(to.get().isDefined()).isTrue();
        assertThat(to.get().get()).isEqualTo(TEST_STRING);
    }

    private String getString() {
        return TEST_STRING;
    }

    private TryOptionT<Integer> convertStringToInt(String s) {
        return TryOptionT.of(() -> Integer.valueOf(s));
    }

    private Try<Integer> convertStringToIntT(String s) {
        return Try.of(() -> Integer.valueOf(s));
    }

    private Option<Integer> convertStringtoIntO(String s) {
        return Option.of(Integer.valueOf(s));
    }

}