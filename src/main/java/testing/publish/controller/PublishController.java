package testing.publish.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.nats.client.JetStreamApiException;
import lombok.extern.slf4j.Slf4j;
import testing.publish.exception.PublishMessageException;
import testing.publish.service.PublishService;

@Controller
@Slf4j
@RequestMapping("/publish")
public class PublishController {

	@Autowired
	private PublishService publishService;

	@PostMapping("/async")
	public ResponseEntity<String> publishAsync(@RequestBody String message, @RequestParam("subject") String subject,
			@RequestParam("url") String url) {

		try {
			log.info(message);
			publishService.publishAsyncMessage(message, subject, url);
			return ResponseEntity.ok("ok");

		} catch (IOException | JetStreamApiException | InterruptedException error) {
			log.error("Error publishing async message. ", error);
			return ResponseEntity.internalServerError().build();
		}

	}

	@PostMapping("/sync")
	public ResponseEntity<String> publishSync(@RequestBody String message, @RequestParam("subject") String subject,
			@RequestParam("url") String url) {

		try {
			log.info(message);
			String response = publishService.publishSyncMessage(message, subject, url);
			return ResponseEntity.ok(response);

		} catch (IOException | InterruptedException | PublishMessageException error) {
			log.error("Error publishing sync message. ", error);
			return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(error.getMessage());
		}

	}
}
