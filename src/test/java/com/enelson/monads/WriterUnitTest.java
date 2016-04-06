package com.enelson.monads;

import com.enelson.monads.algebra.ListMonoid;
import com.enelson.monads.algebra.StringMonoid;
import javaslang.Tuple2;
import javaslang.collection.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class WriterUnitTest {

    @Test
    public void testStaticOf() {
        Writer<StringMonoid, Integer> writer = Writer.of(StringMonoid.of("Two"), 2);

        assertThat(writer).isNotNull();
        assertThat(writer.getLogs().getStr()).isEqualTo(StringMonoid.of("Two").getStr());
        assertThat(writer.getValue()).isEqualTo(2);
    }

    @Test
    public void testMap() {
        Writer<ListMonoid, Integer> writer1 = Writer.of(ListMonoid.of("Two"), 2);
        Writer<ListMonoid, String> writer2  = writer1.map(String::valueOf);

        assertThat(writer2).isNotNull();
        assertThat(writer2.getLogs().getList().size()).isEqualTo(List.of("Two").size());
        assertThat(writer2.getLogs().getList().get(0)).isEqualTo("Two");
        assertThat(writer2.getValue()).isEqualTo("2");
    }

    @Test
    public void testFlatMapStringLog() {
        Writer<StringMonoid, Integer> writer = half(8).flatMap(this::half);

        assertThat(writer).isNotNull();
        assertThat(writer.getLogs().getStr()).isEqualTo("Just halved '8',Just halved '4'");

        Tuple2<StringMonoid, Integer> context = writer.getContext();
        assertThat(context._1.getStr()).isEqualTo(StringMonoid.of("Just halved '8',Just halved '4'").getStr());
        assertThat(context._2).isEqualTo(2);
    }

    @Test
    public void testFlatMapListLog() {
        Writer<ListMonoid, Integer> writer = halfL(8).flatMap(this::halfL);

        assertThat(writer).isNotNull();
        assertThat(writer.getLogs().getList()).isEqualTo(List.of("Just halved '8'", "Just halved '4'"));

        Tuple2<ListMonoid, Integer> context = writer.getContext();
        assertThat(context._1.getList().size()).isEqualTo(ListMonoid.of(List.of("Just halved '8'", "Just halved '4'")).getList().size());
        assertThat(context._2).isEqualTo(2);
    }

    @Test
    public void testGetValue() {
        Writer<StringMonoid, Integer> writer = Writer.of(StringMonoid.of("Two"), 2);
        Integer value = writer.getValue();

        assertThat(value).isEqualTo(2);
    }

    @Test
    public void testGetLogs() {
        Writer<ListMonoid<String>, Integer> writer = Writer.of(ListMonoid.of("Two"), 2);
        ListMonoid<String> logs = writer.getLogs();

        assertThat(logs.getList()).isEqualTo(List.of("Two"));
    }

    @Test
    public void testGetContext() {
        Writer<ListMonoid<String>, Integer> writer = Writer.of(ListMonoid.of("Two"), 2);
        Tuple2<ListMonoid<String>, Integer> context = writer.getContext();

        assertThat(context).isNotNull();
        assertThat(context._1).isInstanceOf(ListMonoid.class);
        assertThat(context._1.getList().size()).isEqualTo(1);
        assertThat(context._1.getList().get(0)).isEqualTo("Two");
        assertThat(context._2).isEqualTo(2);
    }

    @Test
    public void testToString() {
        Writer<ListMonoid<String>, Integer> writer = Writer.of(ListMonoid.of("2!"), 2);

        assertThat(writer).isNotNull();
        assertThat(writer.toString()).isEqualTo("Writer(List(2!), 2)");
    }

    private Writer<StringMonoid, Integer> half(Integer i) {
        return Writer.of(StringMonoid.of("Just halved '"+i+"'"), i / 2);
    }

    private Writer<ListMonoid, Integer> halfL(Integer i) {
        return Writer.of(ListMonoid.of("Just halved '"+i+"'"), i / 2);
    }

}
