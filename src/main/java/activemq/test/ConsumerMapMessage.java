package activemq.test;


import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ConsumerMapMessage {

	public final String SELECTOR_1 = "color = 'blue'";
	public final String SELECTOR_2 = "color = 'blue' AND sal >2000";
	public final String SELECTOR_3 = "receiver = 'A'";
	public final String SELECTOR_4 = "age = 1";

	// 消息连接工厂
	private ConnectionFactory factory;
	// 消息连接对象
	private Connection connection;
	// Session会话对象
	private Session session;
	// 消费者
	private MessageConsumer messageConsumer;
	// 消息存放目标地址
	private Destination destination;
	
	public ConsumerMapMessage(){
		try {
			this.factory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER, 
					ActiveMQConnectionFactory.DEFAULT_PASSWORD, "tcp://localhost:61616");
			this.connection = this.factory.createConnection();
			this.connection.start();
			this.session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
			this.destination = session.createQueue("first");
			this.messageConsumer = this.session.createConsumer(this.destination, this.SELECTOR_4);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void receiver(){
		try {
			this.messageConsumer.setMessageListener(new Listener());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	private class Listener implements MessageListener {

		@Override
		public void onMessage(Message message) {
			try {
				if(message instanceof TextMessage){
					System.out.println(((TextMessage) message).getText());
				}
				if(message instanceof MapMessage){
					MapMessage msg = (MapMessage)message;
					System.out.println(msg.toString());
					System.out.println(msg.getString("name"));
					System.out.println(msg.getString("age"));
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		ConsumerMapMessage m = new ConsumerMapMessage();
		m.receiver();
	}
}
