//package akvelon.zuora.denysenko.flink;
//
//import org.apache.flink.api.common.eventtime.WatermarkStrategy;
//import org.apache.flink.api.common.serialization.SimpleStringSchema;
//import org.apache.flink.api.connector.source.SourceReader;
//import org.apache.flink.connector.base.source.reader.SourceReaderBase;
//import org.apache.flink.connector.kafka.source.KafkaSource;
//import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
//import org.apache.flink.connector.kafka.source.reader.KafkaSourceReader;
//import org.apache.flink.formats.avro.AvroRowDeserializationSchema;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
//import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
//import org.apache.kafka.common.serialization.Serde;
//import org.apache.kafka.common.serialization.Serdes;
//import org.apache.kafka.streams.StreamsBuilder;
//import org.apache.kafka.streams.kstream.Consumed;
//import org.apache.kafka.streams.kstream.KStream;
//import org.apache.kafka.streams.kstream.KTable;
//
//import java.util.Collections;
//
//public class Consumer {
//
//    public void some(){
//        final StreamsBuilder builder = new StreamsBuilder();
//
////        final Serde<Long> longKeySerde = new ChangeEventAwareJsonSerde<>(Long.class);
////        longKeySerde.configure(Collections.emptyMap(),false);
//
//        final Serde<String> stringSerde = Serdes.String();
////        final Serde<Long> longSerde = Serdes.Long();
//
//
//        KStream<String, String> textLines = builder.stream(
//                "streams-plaintext-input",
//                Consumed.with(stringSerde, stringSerde)
//        );
//
//        SourceReaderBase srb = KafkaSourceReader
//
//        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//
//        KafkaDeserializationSchema kds =
//
//
//        KafkaSource<String> source = KafkaSource.<String>builder()
//                .setBootstrapServers("localhost:8083")
//                .setTopics("input-topic")
//                .setGroupId("dbserver1.planning_system.tasks")
//                .setStartingOffsets(OffsetsInitializer.earliest())
//                .setValueOnlyDeserializer(new KafkaAvroDeserializer())
//                .build();
//
//        DataStream<Object> stream = env.fromSource(
//                source,
//                WatermarkStrategy.noWatermarks(),
//                "source");
//
//        DataStream<String> stream1 = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");
//
//
//        SourceReader sr = new KafkaSourceReader();
//        SourceReader sr = KafkaSourceReader;
//
////        new DebeziumAvroDeserializationSchema();
//
////        new FlinkKafkaConsumer010<>();
//
////        KStream<Object, Object> cat = builder.stream("w");
////        KTable<Object, Object> cat1 = builder.table("w");
//    }
//}
