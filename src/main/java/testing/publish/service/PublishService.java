package testing.publish.service;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import io.nats.client.Connection;
import io.nats.client.JetStream;
import io.nats.client.JetStreamApiException;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Options;
import lombok.extern.slf4j.Slf4j;
import testing.publish.exception.PublishMessageException;
import testing.publish.model.AwtoRequest;

@Service
@Slf4j
public class PublishService {

	@Value("${response.time.out.synchronous-messages.seconds}")
	private int responseTimeOutMessagesSeconds;

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
			throws IOException, InterruptedException, PublishMessageException {

		AwtoRequest awtoRequest = createAwtoRequest(message);

		String jsonAwtoRequest = gson.toJson(awtoRequest);

		Message msg = this.getConnection(url).request(subject, jsonAwtoRequest.getBytes(),
				Duration.ofSeconds(this.responseTimeOutMessagesSeconds));

		if (msg == null) {
			String errorMessage = "Message Publisher Error, Response is null (timeout "
					+ this.responseTimeOutMessagesSeconds + " seconds)";
			log.error(errorMessage);
			throw new PublishMessageException(errorMessage);
		}

		byte[] data = msg.getData();
		return new String(data);
	}

	private AwtoRequest createAwtoRequest(String message) {
		AwtoRequest awtoRequest = AwtoRequest.builder().data(message).build();
		return awtoRequest;
	}

}
