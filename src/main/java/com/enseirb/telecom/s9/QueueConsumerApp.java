package com.enseirb.telecom.s9;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.enseirb.telecom.s9.service.ContentServiceImpl;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class QueueConsumerApp {

	/**
	 * Create a queue with the correlation_id of the task, as we can wait the end message
	 * @param queue
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void getQueueMessage(String queue) throws IOException,
			InterruptedException {
		final String QUEUE_NAME = queue;

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		com.rabbitmq.client.Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("x-expires", 86400000);
		channel.queueDeclare(QUEUE_NAME, true, false, true, map);
		
		System.out.println(QUEUE_NAME);
		System.out.println(" [*] Waiting for messages from RabbitMQ.");

		channel.basicConsume(QUEUE_NAME, false, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope,
					BasicProperties properties, byte[] body) throws IOException {
				String result = new String(body);
				JSONObject obj;
				String status = null;
				
				System.out.println(result);
				
				try {
					obj = new JSONObject(result);
					status = obj.getString("status");
					System.out.println(status);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (status.equals("SUCCESS")) {
					// TODO : change the status in DataBase
					System.out.println(QUEUE_NAME);
					ContentServiceImpl contentServiceImpl =new ContentServiceImpl();
					contentServiceImpl.updateContent(QUEUE_NAME);
					
				}
			}
		});

	}
}
