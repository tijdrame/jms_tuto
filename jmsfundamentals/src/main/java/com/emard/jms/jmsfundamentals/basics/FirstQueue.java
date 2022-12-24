package com.emard.jms.jmsfundamentals.basics;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstQueue {

	public static void main(String[] args) {
		InitialContext initialContext = null;
		Connection connection = null;
		try {
			//Ecriture
			initialContext = new InitialContext();
			//meme nom que celui du jndi.properties
			ConnectionFactory cf = (ConnectionFactory)initialContext.lookup("ConnectionFactory");
			connection = cf.createConnection();
			Session session = connection.createSession();
			//meme nom que celui du jndi.properties
			Queue queue = (Queue) initialContext.lookup("queue/myQueue");
			MessageProducer producer = session.createProducer(queue);
			TextMessage message = session.createTextMessage("I am the creater of my destiny");
			producer.send(message);
			System.out.println("Message sent: "+message.getText());
			
			//Lecture
			MessageConsumer consumer = session.createConsumer(queue);
			connection.start();
			TextMessage messageReceive = (TextMessage) consumer.receive(5000);
			System.out.println("Message received: "+messageReceive.getText());
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}finally {
			closeContext(initialContext);
			closeConnection(connection);
		}
	}

	private static void closeConnection(Connection connection) {
		if(connection != null) {
			try {
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		
	}

	private static void closeContext(InitialContext initialContext) {
		if(initialContext != null) {
			try {
				initialContext.close();
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
	}

}
