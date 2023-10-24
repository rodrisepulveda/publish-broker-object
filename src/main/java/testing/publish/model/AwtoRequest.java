package testing.publish.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class AwtoRequest {
	private Object data;
	private Object meta;
}
