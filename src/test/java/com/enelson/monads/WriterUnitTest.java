package com.enelson.monads;

import javaslang.Tuple2;
import javaslang.collection.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class WriterUnitTest {

    @Test
    public void testStaticOf() {
        Writer<Integer> writer = Writer.of("Two", 2);

        assertThat(writer).isNotNull();
        assertThat(writer.getLogs()).isEqualTo(List.of("Two"));
        assertThat(writer.getValue()).isEqualTo(2);
    }

    @Test
    public void testMap() {
        Writer<Integer> writer1 = Writer.of("Two", 2);
        Writer<String> writer2  = writer1.map(String::valueOf);

        assertThat(writer2).isNotNull();
        assertThat(writer2.getLogs()).isEqualTo(List.of("Two"));
        assertThat(writer2.getValue()).isEqualTo("2");
    }

    @Test
    public void testFlatMap() {
        Writer<Integer> writer = half(8).flatMap(i -> half(i));

        assertThat(writer).isNotNull();
        assertThat(writer.getContext()).isEqualTo(new Tuple2<>(List.of("Just halved '8'", "Just halved '4'"), 2));
    }

    @Test
    public void testGetValue() {
        Writer<Integer> writer = Writer.of("Two", 2);
        Integer value = writer.getValue();

        assertThat(value).isEqualTo(2);
    }

    @Test
    public void testGetLogs() {
        Writer<Integer> writer = Writer.of("Two", 2);
        List<String> logs = writer.getLogs();

        assertThat(logs).isEqualTo(List.of("Two"));
    }

    @Test
    public void testGetContext() {
        Writer<Integer> writer = Writer.of("Two", 2);
        Tuple2<List<String>, Integer> context = writer.getContext();

        assertThat(context).isNotNull();
        assertThat(context._1).isEqualTo(List.of("Two"));
        assertThat(context._2).isEqualTo(2);
    }

    @Test
    public void testToString() {
        Writer<Integer> writer = Writer.of("2!", 2);

        assertThat(writer).isNotNull();
        assertThat(writer.toString()).isEqualTo("Writer(2!, 2)");
    }

    private Writer<Integer> half(Integer i) {
        return Writer.of("Just halved '"+i+"'", i / 2);
    }

}
