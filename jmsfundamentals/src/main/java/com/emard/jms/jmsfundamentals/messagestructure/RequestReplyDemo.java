package com.emard.jms.jmsfundamentals.messagestructure;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class RequestReplyDemo {

	public static void main(String[] args) throws NamingException {
		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/requestQueue");
		//Queue replyQueue = (Queue) context.lookup("queue/replyQueue");
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			JMSProducer producer = jmsContext.createProducer();
			TemporaryQueue replyQueue = jmsContext.createTemporaryQueue();
			TextMessage message = jmsContext.createTextMessage("Arise Awake and stop not till the goal is reached");
			message.setJMSReplyTo(replyQueue);
			producer.send(queue, message);
			System.out.println("messageId: "+message.getJMSMessageID());
			//pour enreg le msg original
			Map<String, TextMessage> requestMessages = new HashMap<>();
			requestMessages.put(message.getJMSMessageID(), message);
			
			
			JMSConsumer consumer = jmsContext.createConsumer(queue);
			TextMessage messageReceived = (TextMessage) consumer.receive();
			System.out.println("Message received: "+messageReceived.getText());
			
			JMSProducer replyProducer = jmsContext.createProducer();
			TextMessage replyMessage = jmsContext.createTextMessage("You're awesome!!");
			replyMessage.setJMSCorrelationID(messageReceived.getJMSMessageID());
			replyProducer.send(messageReceived.getJMSReplyTo(), replyMessage);
			
			JMSConsumer replyconsumer = jmsContext.createConsumer(messageReceived.getJMSReplyTo());
			TextMessage replyReceived = (TextMessage) replyconsumer.receive();
			System.out.println("Reply received : "+ replyReceived.getJMSCorrelationID());
			//on aura le msg original depuis la reponse reçu
			System.out.println(requestMessages.get(replyReceived.getJMSCorrelationID()).getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
