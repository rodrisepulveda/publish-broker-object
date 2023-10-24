package testing.publish.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import io.nats.client.Connection;
import io.nats.client.JetStream;
import io.nats.client.JetStreamApiException;
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

	public void publicAsyncMessage(String message, String subject, String url)
			throws IOException, JetStreamApiException, InterruptedException {

		connect(url);

		JetStream jetStream = this.connection.jetStream();

		AwtoRequest awtoRequest = AwtoRequest.builder().data(message).build();

		String jsonAwtoRequest = gson.toJson(awtoRequest);

		log.info("AwtoRequest: " + jsonAwtoRequest);

		jetStream.publish(subject, jsonAwtoRequest.getBytes());

	}

}
