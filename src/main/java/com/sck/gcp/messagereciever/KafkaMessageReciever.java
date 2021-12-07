package com.sck.gcp.messagereciever;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.sck.gcp.gateway.PubSubOutboundGateway;
import com.sck.gcp.processor.FileProcessor;
import com.sck.gcp.service.CloudStorageService;

@Component
public class KafkaMessageReciever {
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageReciever.class);

	@Value("${kafka.inbound.topic:product_topic}")
	private String kafkaTopic;

	@Value("${com.sck.upload.backup.dir:backup}")
	private String backupFolder;

	@Autowired
	private FileProcessor fileProcessor;

	@Autowired
	private CloudStorageService cloudStorageService;

	@Autowired
	private PubSubOutboundGateway messagingGateway;

	@KafkaListener(topics = "${kafka.inbound.topic}", groupId = "id", containerFactory = "productListner")
	public void publish(String stepxml) {
		LOGGER.info("New Entry in Kafka: " + stepxml);

		JSONArray jsonProducts = fileProcessor.convertToJSONs(stepxml);
		jsonProducts.forEach(i -> messagingGateway.sendToPubSub(i.toString()));
		
		//If pubsub publish suucess then write to file
		String jsonl = fileProcessor.convertToJSONL(jsonProducts);
		LOGGER.info("convertToJSONL() completed");

		String uploadFile = cloudStorageService.getFileName("product");
		String backupFilePath = cloudStorageService.getFilePath(backupFolder, uploadFile);

		byte[] arr = jsonl.getBytes();
		cloudStorageService.uploadToCloudStorage(backupFilePath, arr);
		LOGGER.info("File uploaded to bucket with name " + backupFilePath);
		LOGGER.info("Published JSONs to PubSub ");
	}

}
