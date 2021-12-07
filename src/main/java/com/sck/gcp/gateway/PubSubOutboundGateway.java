package com.sck.gcp.gateway;

import org.springframework.integration.annotation.MessagingGateway;

/**
 * interface for sending a message to Pub/Sub.
 */
@MessagingGateway(defaultRequestChannel = "pubSubOutputChannel")
public interface PubSubOutboundGateway {

	void sendToPubSub(String text);
}

