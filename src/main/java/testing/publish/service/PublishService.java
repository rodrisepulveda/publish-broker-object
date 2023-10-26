package testing.publish.service;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

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

	private Map<String, Connection> connetionsMap = new HashMap<>();

	private Gson gson = new Gson();

	private Connection getConnection(String url) throws IOException, InterruptedException {

		Connection connection = connetionsMap.get(url);

		if (connection == null) {

			Options options = new Options.Builder().server(url).build();

			connection = Nats.connect(options);

			connetionsMap.put(url, connection);

		}

		return connection;

	}

	public void publishAsyncMessage(String message, String subject, String url)
			throws IOException, JetStreamApiException, InterruptedException {

		JetStream jetStream = this.getConnection(url).jetStream();

		AwtoRequest awtoRequest = AwtoRequest.builder().data(message).build();

		String jsonAwtoRequest = gson.toJson(awtoRequest);

		log.info("AwtoRequest: " + jsonAwtoRequest);

		jetStream.publish(subject, jsonAwtoRequest.getBytes());

	}

	public String publishSyncMessage(String message, String subject, String url)
			throws IOException, InterruptedException {

		AwtoRequest awtoRequest = createAwtoRequest(message);

		String jsonAwtoRequest = gson.toJson(awtoRequest);

		int timeOutInSeconds = 5;
		Message msg = this.getConnection(url).request(subject, jsonAwtoRequest.getBytes(),
				Duration.ofSeconds(timeOutInSeconds));

		if (msg == null) {
			System.out.println("Response is null (timeout " + timeOutInSeconds + " seconds)");
			return null;
		}

		byte[] data = msg.getData();
		return new String(data);
	}

	private AwtoRequest createAwtoRequest(String message) {
		AwtoRequest awtoRequest = AwtoRequest.builder().data(message).build();
		return awtoRequest;
	}

}
