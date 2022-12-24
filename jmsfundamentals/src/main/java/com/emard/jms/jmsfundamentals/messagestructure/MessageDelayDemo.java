package com.emard.jms.jmsfundamentals.messagestructure;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageDelayDemo {

	public static void main(String[] args) throws NamingException, InterruptedException, JMSException {
		//trouver la queue a partir de jndi
		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/myQueue");
		//try with resources
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			//ecriture
			JMSProducer producer = jmsContext.createProducer();
			producer.setDeliveryDelay(3000);
			producer.send(queue, "Arise Awake and stop not till the goal is reached");
			//lecture
			Message messageReceived = jmsContext.createConsumer(queue).receive(5000);
			System.out.println("Message received: "+messageReceived.getBody(String.class));
		} 
	}

}
