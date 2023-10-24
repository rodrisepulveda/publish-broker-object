package testing.publish.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import io.nats.client.Connection;
import io.nats.client.JetStream;
import io.nats.client.JetStreamApiException;
import io.nats.client.Nats;
import io.nats.client.Options;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PublishService {

	private Connection connection;

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

		jetStream.publish(subject, message.getBytes());

	}

}
