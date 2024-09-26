package tutorBooking.controller;

import java.net.URI;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import tutorBooking.model.User;
import tutorBooking.services.SessionService;
import tutorBooking.services.UserService;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    private SessionService sessionService;

	@GetMapping
	public List<User> getUsers() {
		logger.info("Fetching all users");
		return userService.getAllUsers();
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUsers(@PathVariable("id") long userId) {
	    logger.info("Fetching user with ID: {}", userId);
		User user = userService.getUserById(userId);

		return ResponseEntity.ok(user);
	}

	@PostMapping("/signup")
	public ResponseEntity<User> signUp(@RequestBody User user) {
	    logger.info("Signing up user with username: {}", user.getUsername());
		User newUser = new User(user.getUsername(), user.getPassword(),
        						user.getEmail(), user.getRole());

        User savedUser = userService.createUser(newUser);
        logger.info("User signed up successfully with ID: {}", savedUser.getUserId());
        return ResponseEntity.ok(savedUser);
    }

	@PostMapping
	public ResponseEntity<User> saveUser(@RequestBody User user) {
	    logger.info("Saving user with username: {}", user.getUsername());
		User savedUser = userService.createUser(user);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedUser.getUserId()).toUri();
	    logger.info("User saved successfully with ID: {}", savedUser.getUserId());
		return ResponseEntity.created(location).body(savedUser);
	}

	@PostMapping("/{userId}/sessions/{sessionId}")
	public ResponseEntity<String> enrollUserInSession(
	        @PathVariable long userId,
	        @PathVariable long sessionId) {

		logger.info("Enrolling user with ID: {} in session with ID: {}", userId, sessionId);
	    boolean enrollmentSuccessful;
	    try {
	        enrollmentSuccessful = userService.enrollUserInSession(userId, sessionId);
	        if (enrollmentSuccessful) {
	            logger.info("User with ID: {} successfully enrolled in session with ID: {}", userId, sessionId);
	            return ResponseEntity.ok("User successfully enrolled in session.");
	        } else {
	            logger.warn("User with ID: {} is already enrolled in session with ID: {} or session not found", userId, sessionId);
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is already enrolled in the session or session not found.");
	        }
	    } catch (Exception e) {
	        logger.error("Failed to enroll user with ID: {} in session with ID: {}. Error: {}", userId, sessionId, e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to enroll user in session.");
	    }
	}

}