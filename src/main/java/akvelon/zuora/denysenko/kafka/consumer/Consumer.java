package akvelon.zuora.denysenko.kafka.consumer;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

public class Consumer {

    public void some(){
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> textLines = builder.stream(
                "streams-plaintext-input",
                Consumed.with(stringSerde, stringSerde)
        );


        KStream<Object, Object> cat = builder.stream("w");
        KTable<Object, Object> cat1 = builder.table("w");
    }
}
