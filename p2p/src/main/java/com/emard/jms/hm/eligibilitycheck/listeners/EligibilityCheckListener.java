package com.emard.jms.hm.eligibilitycheck.listeners;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.emard.jms.hm.model.Patient;

public class EligibilityCheckListener implements MessageListener {
	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage) message;
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			InitialContext initialContext = new InitialContext();
			Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
			MapMessage replyMessage = jmsContext.createMapMessage();
			Patient patient = (Patient)objectMessage.getObject();
			if(patient.getInsuranceProvider().equals("Blue Cross Blue Shield")||
					patient.getInsuranceProvider().equals("United Health")) {
				if(patient.getCopay() < 40 && patient.getAmountToBePayed() < 1000) {
					replyMessage.setBoolean("eligible", true);
				}
			}else {
				replyMessage.setBoolean("eligible", false);
			}
			JMSProducer producer = jmsContext.createProducer();
			producer.send(replyQueue, replyMessage);
		} catch (JMSException | NamingException e) {
			e.printStackTrace();
		}
		
	}

}
