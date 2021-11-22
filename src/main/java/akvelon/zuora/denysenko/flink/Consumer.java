package akvelon.zuora.denysenko.flink;

import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Collections;

public class Consumer {

    public void some(){
        final StreamsBuilder builder = new StreamsBuilder();

//        final Serde<Long> longKeySerde = new ChangeEventAwareJsonSerde<>(Long.class);
//        longKeySerde.configure(Collections.emptyMap(),false);

        final Serde<String> stringSerde = Serdes.String();
//        final Serde<Long> longSerde = Serdes.Long();


        KStream<String, String> textLines = builder.stream(
                "streams-plaintext-input",
                Consumed.with(stringSerde, stringSerde)
        );

//        new DebeziumAvroDeserializationSchema();

        new FlinkKafkaConsumer010<>();

//        KStream<Object, Object> cat = builder.stream("w");
//        KTable<Object, Object> cat1 = builder.table("w");
    }
}
