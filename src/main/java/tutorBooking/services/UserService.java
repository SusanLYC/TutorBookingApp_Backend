package tutorBooking.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tutorBooking.exception.UserNotFoundException;
import tutorBooking.model.Session;
import tutorBooking.model.User;
import tutorBooking.repositories.SessionRepository;
import tutorBooking.repositories.UserRepository;

@Service
@Transactional
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, SessionRepository sessionRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.sessionRepository = sessionRepository;
		this.passwordEncoder = passwordEncoder;
	}

	// create
	public User createUser(User user) {
        logger.info("Creating user with username: {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


	public List<User> getAllUsers() {
		logger.info("Fetching all users");
		return userRepository.findAll();
	}

    public boolean enrollUserInSession(long userId, long sessionId) {
    	logger.info("Enrolling user with ID {} in session with ID {}", userId, sessionId);
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Session> sessionOptional = sessionRepository.findById(sessionId);

        if (userOptional.isPresent() && sessionOptional.isPresent()) {
            User user = userOptional.get();
            Session session = sessionOptional.get();

            // Check if the user is already enrolled in the session
            if (!user.getEnrolledClasses().contains(session)) {
                user.getEnrolledClasses().add(session);
                userRepository.save(user); // Save the updated user entity
                logger.info("User with ID: {} successfully enrolled in session with ID: {}", userId, sessionId);
                return true; // Enrolment successful
            } else {
            	logger.warn("User with ID: {} is already enrolled in session with ID:");
            }
        } else {
        	logger.warn("User with ID: {} or session with ID: {} not found", userId, sessionId);
        }
        return false; // Enrolment failed (user or session not found or already enrolled)
    }


	// Read
    public User getUserById(long id) {
    	logger.info("Fetching user with ID: {}", id);
    	Optional<User> userOptional = userRepository.findById(id);

    	return userOptional.orElseThrow(() ->
    		new UserNotFoundException("User with ID " + id + " not found"));
    }


	public long getUserByName(String name) {
		logger.info("Fetching user with username");
        return userRepository.findByUsername(name).get().getUserId();
	}

    // Update
    public void updateUserEmail(long userId, String newEmail) {
    	logger.info("Updating email for user with ID: {}", userId);
        userRepository.updateEmail(userId, newEmail);
    }

    // Delete
    public void deleteUser(Long id) {
    	logger.info("Deleting user with ID: {}", id);
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            // Implement soft delete logic to keep the review data
            user.setDeleted(true);
            userRepository.save(user);
            logger.info("User with ID: {} deleted successfully", id);
		} else {
			logger.warn("User with ID: {} not found", id);
		}
    }

}
