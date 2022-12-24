package com.emard.springjms.senders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

	private final JmsTemplate jmsTemplate;
	@Value("${springjms.myQueue}")
	private String queue;
	public MessageSender(JmsTemplate jmsTemplate){
		this.jmsTemplate = jmsTemplate;
	}
	public void send(String message) {
		//jmsTemplate.setPubSubDomain(true);topic true /false queue defaut
		MessageCreator mc = s -> s.createTextMessage(message);
		jmsTemplate.send(queue, mc);
	}
}
