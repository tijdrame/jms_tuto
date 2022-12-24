package com.emard.jms.jmsfundamentals.messagestructure;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageExpirationDemo {

	public static void main(String[] args) throws NamingException, InterruptedException {
		//trouver la queue a partir de jndi
		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/myQueue");
		Queue expiryQueue = (Queue) context.lookup("queue/expiryQueue");
		//try with resources
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			//ecriture
			JMSProducer producer = jmsContext.createProducer();
			producer.setTimeToLive(2000);//apres 2 sec il expire
			producer.send(queue, "Arise Awake and stop not till the goal is reached");
			Thread.sleep(5000);
			//lecture
			Message messageReceived = jmsContext.createConsumer(queue).receive(5000);
			System.out.println("Message received: "+messageReceived);
			System.out.println("Message expiry: "+jmsContext.createConsumer(
					expiryQueue).receiveBody(String.class));
		} 
	}

}
