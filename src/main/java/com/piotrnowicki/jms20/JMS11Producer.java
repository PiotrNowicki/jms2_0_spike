package com.piotrnowicki.jms20;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.UUID;


@Path("/producer")
@Stateless
public class JMS11Producer {

    @Resource(lookup = "jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/queue/myqueue")
    private Queue queue;

    @Path("/jms11")
    @GET
    public String produce() {

        String status = "OK";

        // If we'll use this connection in more than one methods or want to reuse connection - it can be moved to @PostConstruct / @PreDestroy
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();

            // Doesn't matter what arguments we pass - the container defines transactional behavior and acknowledge mode
            Session session = connection.createSession(true, 0);

            MessageProducer producer = session.createProducer(queue);

            BusinessObject payload = new BusinessObject(UUID.randomUUID().toString());

            ObjectMessage message = session.createObjectMessage();
            message.setObject(payload);

            producer.send(message);

        } catch (JMSException e) {
            status = e.getMessage();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    status = e.getMessage();
                }
            }
        }

        return status;
    }
}
