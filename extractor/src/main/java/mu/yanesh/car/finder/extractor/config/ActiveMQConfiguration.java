package mu.yanesh.car.finder.extractor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class ActiveMQConfiguration {

//    @Qualifier("jmsConnectionFactory")
//    private final ConnectionFactory connectionFactory;
//    private final ObjectMapper objectMapper;
    //TODO: Complete setup for activemq

}
