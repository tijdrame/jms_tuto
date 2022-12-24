package com.emard.jms.jmsfundamentals.messagestructure;

import javax.jms.BytesMessage;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.jms.StreamMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessagePropertiesDemo {

	public static void main(String[] args) throws NamingException, InterruptedException, JMSException {
		//trouver la queue a partir de jndi
		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/myQueue");
		//try with resources
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			//ecriture
			JMSProducer producer = jmsContext.createProducer();
			BytesMessage bytesMessage = jmsContext.createBytesMessage();
			bytesMessage.writeUTF("John");
			bytesMessage.writeLong(123l);
			
			StreamMessage streamMessage = jmsContext.createStreamMessage();
			streamMessage.writeBoolean(true);
			streamMessage.writeFloat(2.5f);
			
			MapMessage mapMessage = jmsContext.createMapMessage();
			mapMessage.setBoolean("isCreditAvailable", true);
			
			//ObjectMessage objectMessage = jmsContext.createObjectMessage();
			//objectMessage.setObject(new Patient(1, "John Doe"));
			
			producer.send(queue, new Patient(1, "John Doe"));
			//lecture
			Patient patient = (Patient) jmsContext.createConsumer(queue).receiveBody(Patient.class);
			//Patient patient = (Patient) messageReceived.getObject();
			System.out.println("Message id: "+patient.getId());
			System.out.println("Message name: "+patient.getName());
			//MapMessage messageReceived = (MapMessage) jmsContext.createConsumer(queue).receive(5000);
			//System.out.println("Message bool: "+messageReceived.getBoolean("isCreditAvailable"));
			//StreamMessage messageReceived = (StreamMessage) jmsContext.createConsumer(queue).receive(5000);
			//System.out.println("Message bool: "+messageReceived.readBoolean());
			//System.out.println("Message float: "+messageReceived.readFloat());
			//BytesMessage messageReceived = (BytesMessage) jmsContext.createConsumer(queue).receive(5000);
			//System.out.println("Message utf: "+messageReceived.readUTF());
			//System.out.println("Message long: "+messageReceived.readLong());
		} 
	}

}
