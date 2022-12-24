package com.emard.jms.security;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.emard.jms.hr.Employee;

public class SecurityApp {

	public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
		InitialContext context = new InitialContext();
		Topic topic = (Topic) context.lookup("topic/empTopic");
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()){
			jmsContext.setClientID("securityApp");
			JMSConsumer consumer = jmsContext.createDurableConsumer(topic, "subscription1");
			consumer.close();//simulate going down
			Thread.sleep(10000);
			//app is up again
			consumer = jmsContext.createDurableConsumer(topic, "subscription1");
			Message message = consumer.receive();
			Employee employee = message.getBody(Employee.class);
			System.out.println("Message received from SecApp: "+employee);
			consumer.close();
			jmsContext.unsubscribe("subscription1");
		}
	}
}
