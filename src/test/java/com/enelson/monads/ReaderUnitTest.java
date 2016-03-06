package com.enelson.monads;

import javaslang.Tuple2;
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

    @Test
    public void testReaderZip() {
        Reader<String, Integer> reader1 = Reader.pure(2);
        Reader<String, String> reader2  = Reader.pure("3");
        Reader<String, Tuple2<Integer, String>> reader3 = reader1.zip(reader2);
        Tuple2<Integer, String> result = reader3.apply("test");

        assertThat(result).isNotNull();
        assertThat(result._1).isEqualTo(2);
        assertThat(result._2).isEqualTo("3");
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
