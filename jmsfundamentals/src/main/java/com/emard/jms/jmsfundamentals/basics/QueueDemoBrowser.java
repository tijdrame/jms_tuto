package com.emard.jms.jmsfundamentals.basics;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class QueueDemoBrowser {

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
			TextMessage message1 = session.createTextMessage("Message 1");
			TextMessage message2 = session.createTextMessage("Message 2");
			producer.send(message1);
			producer.send(message2);
			QueueBrowser browser = session.createBrowser(queue);
			Enumeration<?> messagesEnum = browser.getEnumeration();
			//parcourir (browser) les messages
			while (messagesEnum.hasMoreElements()) {
				TextMessage eachMessage = (TextMessage) messagesEnum.nextElement();
				System.out.println("Browsing :"+eachMessage.getText());
			}
			
			//Lecture
			MessageConsumer consumer = session.createConsumer(queue);
			connection.start();
			TextMessage messageReceive = (TextMessage) consumer.receive(5000);
			System.out.println("Message received: "+messageReceive.getText());
			messageReceive = (TextMessage) consumer.receive(5000);
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
