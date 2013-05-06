package com.piotrnowicki.jms20;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Date;
import java.util.UUID;


@Path("/producer")
@Stateless
public class JMS20Producer {

    @Resource(lookup = "jms/queue/myqueue")
    private Queue queue;

    @Inject
    private JMSContext jmsContext;

    @Path("/jms20")
    @GET
    public String produce() {
        BusinessObject payload = new BusinessObject(UUID.randomUUID().toString(), new Date());

        jmsContext.createProducer().send(queue, payload);

        return "OK";
    }
}
