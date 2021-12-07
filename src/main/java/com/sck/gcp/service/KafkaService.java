package com.sck.gcp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sck.gcp.processor.FileProcessor;

@Service
public class KafkaService {
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaService.class);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private FileProcessor fileProcessor;

	@Value("${com.sck.upload.backup.dir:backup}")
	private String backupFolder;

	@Value("${kafka.inbound.topic:product_topic}")
	private String kafkaTopic;

	public String publishToKafka(MultipartFile file) {

		String xml = fileProcessor.readFile(file);

		LOGGER.info("Converted file to JSON String");
		kafkaTemplate.send(kafkaTopic, xml);

		return "Published to Kafka successfully";

	}
}
