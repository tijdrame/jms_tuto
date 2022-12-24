package com.emard.jms.hm.clinicals;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.emard.jms.hm.model.Passenger;

public class CheckInApp {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
		Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()){
			JMSProducer producer = jmsContext.createProducer();
			ObjectMessage objectMessage = jmsContext.createObjectMessage();
			Passenger passenger = new Passenger();
			passenger.setId(123);passenger.setFirstName("Tidiane");
			passenger.setLastName("Dramé");passenger.setEmail("tijdrame@gmail.com");
			passenger.setPhone("776055641");
			objectMessage.setObject(passenger);
			producer.send(requestQueue, objectMessage);
			
			JMSConsumer consumer = jmsContext.createConsumer(replyQueue);
			ObjectMessage replyMessage = (ObjectMessage) consumer.receive(30000);
			System.out.println("The passenger is checked? : "+ replyMessage.getBooleanProperty("checked"));
			System.out.println("The passenger : "+((Passenger)replyMessage.getObject()).toString());
		}
	}

}
