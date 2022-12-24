package com.emard.jms.hm.eligibilitycheck;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.emard.jms.hm.eligibilitycheck.listeners.PassengerCheckListener;

public class ReservationSystemApp {

	public static void main(String[] args) throws InterruptedException, NamingException {
		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()){
			JMSConsumer consumer = jmsContext.createConsumer(requestQueue);
			/*JMSConsumer consumer2 = jmsContext.createConsumer(requestQueue);
			for(int i=1; i<=10;i++) {
				System.out.println("Consumer 1: "+consumer1.receive());
				System.out.println("Consumer 2: "+consumer2.receive());
			}*/
			//listner qui permet de faire l'async
			consumer.setMessageListener(new PassengerCheckListener());
			//pas besoin si c une app server mais ici on a juste un main
			Thread.sleep(10000);
		}
	}

}
