package com.enelson.monads;

import javaslang.Tuple2;
import javaslang.collection.HashMap;
import javaslang.collection.Map;
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
        State<String, Integer> state1 = State.of(s -> new Tuple2<>(s, Integer.parseInt(s)+1));
        State<String, String> r = state1.flatMap(this::convert);
        System.out.println("--> _1: "+r.apply("2")._1);
        System.out.println("--> _2: "+r.apply("2")._2);

        assertThat(state1).isNotNull();
        assertThat(r).isNotNull();
        assertThat(r.apply("2")._2).isEqualTo("3");
    }

    @Test
    public void testStory() {
        State<Map<String, Integer>, Integer> r = countWords("this is one two three");

        State<Map<String, Integer>, Integer> state = countWords("This is the first sentence")
                                                         .flatMap(s -> countWords("This is the second sentence"))
                                                         .flatMap(s -> countWords("This is the third sentence"));

        Tuple2<Map<String, Integer>, Integer> result = state.apply(HashMap.empty());
        System.out.println(result._1);
        System.out.println(result._2);
    }

    private State<String, String> convert(Integer i) {
        return State.of(s -> new Tuple2<>(s, i+""));
    }

    private State<Map<String, Integer>, Integer> countWords(String sentence) {
        return State.modify(map -> {
            System.out.println("--> In: "+map );
            for( String word : sentence.trim().split(" ") ) {
                Integer currCount = map.get(word).getOrElse(0);
                map = map.put(word, currCount+1);
            }
            System.out.println("--> Out: "+map );
            return map;
        });
    }

}
