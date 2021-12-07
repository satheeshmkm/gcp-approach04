package com.sck.gcp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.outbound.PubSubMessageHandler;

@Configuration
public class PubSubConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(PubSubConfig.class);

	@Value("${com.sck.pubsub.topic.name}")
	private String topic;

	@Bean
	@ServiceActivator(inputChannel = "pubSubOutputChannel")
	public MessageHandler messageSender(PubSubTemplate pubsubTemplate) {
		PubSubMessageHandler adapter = new PubSubMessageHandler(pubsubTemplate, topic);
		adapter.setPublishCallback(new ListenableFutureCallback<String>() {
			@Override
			public void onFailure(Throwable ex) {
				LOGGER.info("There was an error sending the message.");
			}

			@Override
			public void onSuccess(String result) {
				LOGGER.info("Message was sent successfully. ::" + result);
			}
		});

		return adapter;
	}

}
