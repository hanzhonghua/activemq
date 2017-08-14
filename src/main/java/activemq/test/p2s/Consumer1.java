package activemq.test.p2s;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.spring.ActiveMQConnectionFactory;


public class Consumer1 {

	// 连接工厂
	private ConnectionFactory factory;
	// 连接对象
	private Connection connection;
	// 会话
	private Session session;
	// 消息生产者
	private MessageConsumer messageConsumer;
	// 消息目的地
	private Destination destination;
	
	public Consumer1(){
		try {
			this.factory = new org.apache.activemq.ActiveMQConnectionFactory(
						ActiveMQConnectionFactory.DEFAULT_USER,
						ActiveMQConnectionFactory.DEFAULT_PASSWORD,
						"tcp://localhost:61616");
			this.connection = this.factory.createConnection();
			connection.start();
			// 根据连接对象创建Session，指定为不开启事务，自动签收
			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// 创建消费者
			this.destination = session.createTopic("firstTopic");
			this.messageConsumer = session.createConsumer(destination);
			
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
					System.out.println("Consumer1接收消息");
					System.out.println(((TextMessage) message).getText());
				}
				
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		Consumer1 c1 = new Consumer1();
		c1.receiver();
	}
}
