package tutorBooking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SessionNotFoundException extends RuntimeException{

	public SessionNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SessionNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public SessionNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public SessionNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public SessionNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}


}
