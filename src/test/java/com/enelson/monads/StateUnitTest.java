package com.enelson.monads;

import javaslang.Tuple2;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class StateUnitTest {

    @Test
    public void testStaticOf() {
        State<String, Integer> state = State.of(s -> new Tuple2<>(s, 2));

        assertThat(state).isNotNull();
    }

    @Test
    public void testStaticGet() {
        State<String, String> state = State.get();
        Tuple2<String, String> r = state.apply("test");

        assertThat(state).isNotNull();
        assertThat(r).isNotNull();
        assertThat(r._1).isEqualTo("test");
        assertThat(r._2).isEqualTo("test");
    }

    @Test
    public void testStaticGetS() {
        State<String, String> state = State.gets(s -> s+"-"+s);
        Tuple2<String, String> r = state.apply("test");

        assertThat(state).isNotNull();
        assertThat(r).isNotNull();
        assertThat(r._1).isEqualTo("test");
        assertThat(r._2).isEqualTo("test-test");
    }

    @Test
    public void testStaticPut() {
        State<String, String> state = State.put("newState");
        Tuple2<String, String> r = state.apply("test");

        assertThat(state).isNotNull();
        assertThat(r).isNotNull();
        assertThat(r._1).isEqualTo("newState");
        assertThat(r._2).isNull();
    }

    @Test
    public void testStaticModify() {
        State<String, String> state = State.modify(s -> s+"-"+s);
        Tuple2<String, String> r = state.apply("test");

        assertThat(state).isNotNull();
        assertThat(r).isNotNull();
        assertThat(r._1).isEqualTo("test-test");
        assertThat(r._2).isNull();
    }

    @Test
    public void testMap() {
        State<String, Integer> state = State.of(s -> new Tuple2<>(s, 2));
        State<String, String> mappedState = state.map(i -> i.toString());

        assertThat(state).isNotNull();
        assertThat(mappedState).isNotNull();
        assertThat(mappedState.apply("s")).isInstanceOf(Tuple2.class);
        assertThat(mappedState.apply("s")._2).isEqualTo("2");
    }

    @Test
    public void testFlatMap() {
        State<String, Integer> state1 = State.of(s -> new Tuple2<>(s, Integer.parseInt(s)));
        State<String, String> r = state1.flatMap(this::convert);

        assertThat(state1).isNotNull();
        assertThat(r).isNotNull();
        assertThat(r.apply("2")._2).isEqualTo("2");
    }

    private State<String, String> convert(Integer i) {
        return State.of((String s) -> new Tuple2<>(s, i+""));
    }

}
