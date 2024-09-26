package tutorBooking.services;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import tutorBooking.model.User;

@Service
public class UserWebClient {

	private final WebClient webClient;

	public UserWebClient() {
		super();
		this.webClient = WebClient.builder()
				.baseUrl("http://localhost:8080/")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}

	public List<User> getUsers() {
		return webClient.get().uri("/api/v1/users").retrieve().bodyToFlux(User.class).collectList().block();
	}
}
