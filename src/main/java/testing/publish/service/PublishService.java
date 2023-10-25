package testing.publish.service;

import java.io.IOException;
import java.time.Duration;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import io.nats.client.Connection;
import io.nats.client.JetStream;
import io.nats.client.JetStreamApiException;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Options;
import lombok.extern.slf4j.Slf4j;
import testing.publish.model.AwtoRequest;

@Service
@Slf4j
public class PublishService {

	private Connection connection;

	private Gson gson = new Gson();

	private void connect(String url) throws IOException, InterruptedException {

		if (this.connection != null) {

			return;
		}

		Options options = new Options.Builder().server(url).build();

		this.connection = Nats.connect(options);

	}

	public void publishAsyncMessage(String message, String subject, String url)
			throws IOException, JetStreamApiException, InterruptedException {

		connect(url);

		JetStream jetStream = this.connection.jetStream();

		AwtoRequest awtoRequest = AwtoRequest.builder().data(message).build();

		String jsonAwtoRequest = gson.toJson(awtoRequest);

		log.info("AwtoRequest: " + jsonAwtoRequest);

		jetStream.publish(subject, jsonAwtoRequest.getBytes());

	}
	
	public String publishSyncMessage(String message, String subject, String url)
			throws IOException, InterruptedException {

		int timeOutInSeconds = 5;
		Message msg = this.connection.request(subject, message, Duration.ofSeconds(timeOutInSeconds));

		if (msg == null) {
			System.out.println("Response is null (timeout " + timeOutInSeconds + " seconds)");
			return null;
		}

		byte[] data = msg.getData();
		return new String(data);
	}

}
