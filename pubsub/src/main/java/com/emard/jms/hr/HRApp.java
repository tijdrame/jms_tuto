package com.emard.jms.hr;

import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class HRApp {

	public static void main(String[] args) throws NamingException {
		InitialContext context = new InitialContext();
		Topic topic = (Topic) context.lookup("topic/empTopic");
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()){
			Employee emp = new Employee();
			emp.setId(123);emp.setFirstName("john");
			emp.setLastName("Doe");emp.setDesignation("Software architect");
			emp.setEmail("johndoe@gmail.com");emp.setPhone("+14563625689");
			for (int i = 1; i <= 10; i++) {
				jmsContext.createProducer().send(topic, emp);
			}
			System.out.println("Message sent");
		}
	}

}
