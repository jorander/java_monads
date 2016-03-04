package com.enelson.monads;

import javaslang.collection.List;
import javaslang.collection.Seq;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class ReaderUnitTest {

    @Test
    public void testStaticOf() {
        Reader<String, Integer> reader = Reader.of(Integer::parseInt);
        assertThat(reader).isNotNull();

        Integer i = reader.apply("2");
        assertThat(i).isEqualTo(2);
    }

    @Test
    public void testStaticPure() {
        Reader<String, Integer> reader = Reader.pure(2);
        assertThat(reader).isNotNull();

        Integer i = reader.apply("3");
        assertThat(i).isEqualTo(2);
    }

//    @Test
//    public void testStaticSequence() {
//        Reader<String, Integer> reader1 = Reader.of(s -> 1);
//        Reader<String, Integer> reader2 = Reader.of(s -> 2);
//        Reader<String, Integer> reader3 = Reader.of(s -> 3);
//
//        Seq<Reader<String, Integer>> r = List.of(reader1, reader2, reader3);
//        Reader<String, List<Integer>> readerComposed = Reader.<String, Integer>sequence(r);
//    }

}
