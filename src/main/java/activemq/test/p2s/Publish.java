package activemq.test.p2s;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.spring.ActiveMQConnectionFactory;

/**
 * 发布消息到Topic
 */
public class Publish {

	// 连接工厂
	private ConnectionFactory factory;
	// 连接对象
	private Connection connection;
	// 会话
	private Session session;
	// 消息生产者
	private MessageProducer messageProducer;
	
	public Publish(){
		try {
			this.factory = new org.apache.activemq.ActiveMQConnectionFactory(
						ActiveMQConnectionFactory.DEFAULT_USER,
						ActiveMQConnectionFactory.DEFAULT_PASSWORD,
						"tcp://localhost:61616");
			this.connection = this.factory.createConnection();
			connection.start();
			// 根据连接对象创建Session，指定为不开启事务，自动签收
			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// 创建生产者(暂时不指定消息发送目的地，在发送消息时候才指定)
			this.messageProducer = session.createProducer(null);
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public void send(){
		try {
			Destination destination = this.session.createTopic("firstTopic");
			TextMessage message = session.createTextMessage("我是topic内容");
			messageProducer.send(destination, message);
			
			if(connection != null){
				connection.close();
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		Publish p = new Publish();
		p.send();
	}
}
