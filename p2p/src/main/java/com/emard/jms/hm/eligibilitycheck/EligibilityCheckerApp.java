package com.emard.jms.hm.eligibilitycheck;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.emard.jms.hm.eligibilitycheck.listeners.EligibilityCheckListener;

public class EligibilityCheckerApp {

	public static void main(String[] args) throws NamingException, 
	JMSException, InterruptedException {
		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext("eligibilityuser", "elibilitypass")){
			JMSConsumer consumer1 = jmsContext.createConsumer(requestQueue);
			JMSConsumer consumer2 = jmsContext.createConsumer(requestQueue);
			for(int i=1; i<=10;i++) {
				System.out.println("Consumer 1: "+consumer1.receive());
				System.out.println("Consumer 2: "+consumer2.receive());
			}
			//listner qui permet de faire l'async
			//consumer.setMessageListener(new EligibilityCheckListener());
			//pas besoin si c une app server mais ici on a juste un main
			//Thread.sleep(10000);
		}
	}

}
