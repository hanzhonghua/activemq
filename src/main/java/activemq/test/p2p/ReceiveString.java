package activemq.test.p2p;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 消费者
 * 1：首先建立连接，用户名、密码、tcp地址
 * 2：创建连接，然后打开连接
 * 3：创建Session会话，关联着生产者消费者以及消息的目的地，创建Session时可指定是否需要事务以及签收方式
 * 4：通过Session创建Destination，用来存放消息的目的地
 * 5：创建消费者
 * 6：使用消费者消费消息
 */
public class ReceiveString {

	public static void main(String[] args) throws Exception {
		// 1：建立ConnectionFactory对象，需要用户名、密码已经连接地址(默认tcp://localhost:61616)
		ConnectionFactory factory = new ActiveMQConnectionFactory(
				ActiveMQConnectionFactory.DEFAULT_USER,
				ActiveMQConnectionFactory.DEFAULT_PASSWORD,
				"tcp://localhost:61616");

		// 2：通过ConnectionFactory对象创建Connection连接，并调用start方法开启连接，默认是关闭的
		Connection connection = factory.createConnection();
		connection.start();

		// 3：通过Connection对象创建Session会话(上下文环境对象)，用户接受消息，参数1是否启用事务，参数2为签收模式
//		Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
		// 手动签收
		Session session = connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);

		/*
		 * 4：通过Session创建Destination对象，指一个客户端用来指定生产消息目标和消费消息来源对象，存放消息的地方
		 * 在PTP模式中，Destination被称作Queue，在Pub/Sub中，被称作Topic既是主题，可以出现多个
		 */
		Destination destination = session.createQueue("testQueue"); // 队列的名字

		/*
		 * 5：通过Session对象创建消息的发送和接受对象(生产者和消费者)MessageProducer/MessageConsumer，消息放入
		 * 上一步创建的Destination
		 */
		MessageConsumer consumer = session.createConsumer(destination);

		while(true){
			TextMessage msg = (TextMessage) consumer.receive();
			if(msg == null){
				break;
			}
			// 设置手动签收，才可以完成消息的消费，如果不设置的话，只能读取消息，但是消费不了
			msg.acknowledge();
			System.out.println("收到的内容：" + msg.getText());
		}
		
		if (connection != null) {
			connection.close();
		}
	}
}
