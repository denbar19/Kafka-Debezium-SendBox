package akvelon.zuora.denysenko.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 *
 * @author Denysenko Stanislav
 * <a href="mailto:stanislav.denisenko@akvelon.com">mail</a>
 */
@SpringBootApplication
@EnableKafka
public class KafkaDebeziumSendBox {

    public static void main(String[] args) {
        SpringApplication.run(KafkaDebeziumSendBox.class, args);
    }

}
