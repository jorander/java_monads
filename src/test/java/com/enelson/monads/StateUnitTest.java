package com.enelson.monads;

import javaslang.Tuple2;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class StateUnitTest {

    @Test
    public void testStaticOf() {

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
        State<String, Integer> state1 = State.of(s -> new Tuple2<>(s, 2));
        State<String, String>  state2 = State.of(s -> new Tuple2<>(s, "2"));

    }

}
