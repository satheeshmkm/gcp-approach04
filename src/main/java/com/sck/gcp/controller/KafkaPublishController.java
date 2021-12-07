package com.sck.gcp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sck.gcp.service.KafkaService;

import io.swagger.annotations.ApiOperation;

@RestController
public class KafkaPublishController {
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaPublishController.class);

	@Autowired
	private KafkaService kafkaService;

	@ApiOperation("Upload the XML file, convert it to JSON and upload to BigQuery Table")
	@PostMapping(value = "/process/convertxml/publish", consumes = { "multipart/form-data" })
	public ResponseEntity<?> uploadAndPublish(@RequestParam(value = "file", required = true) MultipartFile uploadfile) {
		if (uploadfile.isEmpty()) {
			LOGGER.error("File is not passed");
			return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
		}

		try {
			String response = kafkaService.publishToKafka(uploadfile);
			return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Exception happened", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

}
