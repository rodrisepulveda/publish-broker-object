package testing.publish.exception;

public class PublishMessageException extends Exception {

	public PublishMessageException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PublishMessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public PublishMessageException(String message) {
		super(message);
	}

	public PublishMessageException(Throwable cause) {
		super(cause);
	}
}
