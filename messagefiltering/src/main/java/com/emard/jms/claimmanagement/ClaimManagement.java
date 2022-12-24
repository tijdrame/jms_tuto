package com.emard.jms.claimmanagement;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class ClaimManagement {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/claimQueue");
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()){
			JMSProducer producer = jmsContext.createProducer();
			//String filter = "doctorName LIKE 'H%'";
			//String filter = "insuranceProvider IN('blue cross', 'american')";
			String filter = "claimAmount %10=0";
			JMSConsumer consumer = jmsContext.createConsumer(requestQueue, filter);
			
			ObjectMessage objectMessage = jmsContext.createObjectMessage();
			//objectMessage.setIntProperty("hospitalId", 1);
			//objectMessage.setDoubleProperty("claimAmount", 1000);
			
			Claim claim = new Claim();claim.setHospitalId(1);
			claim.setDoctorName("HJohn Doe");claim.setClaimAmount(1000d);
			claim.setInsuranceProvider("blue cross");claim.setDoctorType("gyna");
			objectMessage.setDoubleProperty("claimAmount", claim.getClaimAmount());
			objectMessage.setObject(claim);
			producer.send(requestQueue, objectMessage);
			Claim claimReceived = consumer.receiveBody(Claim.class);
			System.out.println("claim received= "+claimReceived);
		}
	}

}
