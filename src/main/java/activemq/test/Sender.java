package activemq.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

//发送者
public class Sender {

	public static void main(String[] args) throws Exception{
		// 1：建立ConnectionFactory对象，需要用户名、密码已经连接地址(默认tcp://localhost:61616)
		ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER, 
				ActiveMQConnectionFactory.DEFAULT_PASSWORD, "tcp://localhost:61616");
		
		// 2：通过ConnectionFactory对象创建Connection连接，并调用start方法开启连接，默认是关闭的
		Connection connection = factory.createConnection();
		connection.start();
		
		// 3：通过Connection对象创建Session会话(上下文环境对象)，用户接受消息，参数1是否启用事务，参数2为签收模式
		Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
		
		/*
		 * 4：通过Session创建Destination对象，指一个客户端用来指定生产消息目标和消费消息来源对象，存放消息的地方
		 * 在PTP模式中，Destination被称作Queue，在Pub/Sub中，被称作Topic既是主题，可以出现多个
		 */
		Destination destination = session.createQueue("testQueue"); // 队列的名字
		
		/*
		 * 5：通过Session对象创建消息的发送和接受对象(生产者和消费者)MessageProducer/MessageConsumer，消息放入
		 *	上一步创建的Destination
		 */
		MessageProducer producer = session.createProducer(destination);
		
		// 6：使用生产者的setDeliveryMode方法设置持久和非持久特性
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		
		// 7：用JMS规范的TextMessage形式创建数据(通过Session对象)，使用生产者的send方法发送数据
		TextMessage message = session.createTextMessage();
		for (int i = 0; i < 5; i++) {
			message.setText("测试发送消息");
			producer.send(message);
		}
		
		if(connection != null){
			connection.close();
		}
	}
}
