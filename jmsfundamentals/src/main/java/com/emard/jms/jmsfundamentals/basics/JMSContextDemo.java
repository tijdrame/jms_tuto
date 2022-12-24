package com.emard.jms.jmsfundamentals.basics;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class JMSContextDemo {

	public static void main(String[] args) throws NamingException {
		//trouver la queue a partir de jndi
		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/myQueue");
		//try with resources
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			//ecriture
			jmsContext.createProducer().send(queue, "Arise Awake and stop not till the goal is reached");
			//lecture
			String messageReceived = jmsContext.createConsumer(queue).receiveBody(String.class);
			System.out.println("Message received: "+messageReceived);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
