package com.emard.jms.hm.eligibilitycheck.listeners;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.emard.jms.hm.model.Passenger;

public class PassengerCheckListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage) message;
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			InitialContext initialContext = new InitialContext();
			Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
			ObjectMessage replyMessage = jmsContext.createObjectMessage();
			Passenger passenger = (Passenger)objectMessage.getObject();
			replyMessage.setBooleanProperty("checked", true);
			replyMessage.setObject(passenger);
			JMSProducer producer = jmsContext.createProducer();
			producer.send(replyQueue, replyMessage);
		}catch (JMSException | NamingException e) {
			e.printStackTrace();
		}
	}

}
