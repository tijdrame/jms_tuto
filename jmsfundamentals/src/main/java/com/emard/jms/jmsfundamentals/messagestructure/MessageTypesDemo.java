package com.emard.jms.jmsfundamentals.messagestructure;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageTypesDemo {

	public static void main(String[] args) throws NamingException, InterruptedException, JMSException {
		//trouver la queue a partir de jndi
		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/myQueue");
		//try with resources
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			//ecriture
			JMSProducer producer = jmsContext.createProducer();
			TextMessage textMessage = jmsContext.createTextMessage("Arise Awake and stop not till the goal is reached");
			textMessage.setBooleanProperty("loggedIn", true);
			textMessage.setStringProperty("userToken", "abc123");
			textMessage.setStringProperty("username", "tidiane");
			
			producer.send(queue, textMessage);
			//lecture
			Message messageReceived = jmsContext.createConsumer(queue).receive(5000);
			System.out.println("Message received: "+messageReceived.getBody(String.class));
			System.out.println("Boolean props: "+messageReceived.getBooleanProperty("loggedIn"));
			System.out.println("userToken props: "+messageReceived.getStringProperty("userToken"));
			System.out.println("username props: "+messageReceived.getStringProperty("username"));
		} 
	}

}
