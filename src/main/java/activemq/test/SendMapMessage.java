package activemq.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class SendMapMessage {

	// 连接工厂
	private ConnectionFactory factory;
	// 连接对象
	private Connection connection;
	// Session对象
	private Session session;
	// 生产者
	private MessageProducer producer;
	
	public SendMapMessage(){
		try {
			this.factory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER, 
					ActiveMQConnectionFactory.DEFAULT_PASSWORD, "tcp://localhost:61616");
			this.connection = this.factory.createConnection();
			this.connection.start();
			this.session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
			this.producer = this.session.createProducer(null);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public void send1(){
		try {
			Destination destination = this.session.createQueue("first");
			this.producer = session.createProducer(null);
			MapMessage msg1 = this.session.createMapMessage();
			msg1.setString("name", "张三");
			msg1.setString("age", "18");
			MapMessage msg2 = this.session.createMapMessage();
			msg2.setString("name", "李四");
			msg2.setString("age", "25");
			MapMessage msg3 = this.session.createMapMessage();
			msg3.setString("name", "王五");
			msg3.setString("age", "16");
			MapMessage msg4 = this.session.createMapMessage();
			msg4.setString("name", "赵六");
			msg4.setString("age", "30");
			
			this.producer.send(destination, msg1, DeliveryMode.NON_PERSISTENT, 2, 1000*60*10);
			this.producer.send(destination, msg2, DeliveryMode.NON_PERSISTENT, 4, 1000*60*10);
			this.producer.send(destination, msg3, DeliveryMode.NON_PERSISTENT, 8, 1000*60*10);
			this.producer.send(destination, msg4, DeliveryMode.NON_PERSISTENT, 6, 1000*60*10);
			
			if(this.connection != null){
				this.connection.close();
			}
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		SendMapMessage s = new SendMapMessage();
		s.send1();
	}
}
