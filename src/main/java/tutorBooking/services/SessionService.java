package tutorBooking.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tutorBooking.enums.RoleType;
import tutorBooking.enums.StatusType;
import tutorBooking.exception.ResourceNotFoundException;
import tutorBooking.exception.SessionNotFoundException;
import tutorBooking.model.Session;
import tutorBooking.model.User;
import tutorBooking.repositories.SessionRepository;
import tutorBooking.repositories.UserRepository;

@Service
public class SessionService {

	private static final Logger logger = LoggerFactory.getLogger(SessionService.class);

    @Autowired
    private SessionRepository sessionRepository;
    private UserRepository userRepository;


    // Constructor
	public SessionService(SessionRepository sessionRepository, UserRepository userRepository) {
		this.sessionRepository = sessionRepository;
		this.userRepository = userRepository;
	}


    // Fetch sessions based on the user's role
    public List<Session> getUserEnrolledOrOwnedSessions(long userId) {
    	logger.info("Fetching sessions based on user's role");
        User user = userRepository.findById(userId)
        		.orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == RoleType.STUDENT) {
        	logger.info("Fetching sessions for student");
            return sessionRepository.findByStudents_UserId(userId);
        } else if (user.getRole() == RoleType.TUTOR) {
        	logger.info("Fetching sessions for tutor");
            return sessionRepository.findByTutor_UserId(userId);
        } else {
        	logger.error("Invalid role type");
            throw new IllegalArgumentException("Invalid role type");
        }
    }

	// Service methods
    public List<Session> getAllSessions() {
    	logger.info("Fetching all sessions");
        return sessionRepository.findAll();
    }

    public Session getSessionById(long id) {
    	logger.info("Fetching session by ID");
    	Optional <Session> optionalSession = sessionRepository.findById(id);

    	return optionalSession.orElseThrow(() ->
    	    new SessionNotFoundException("Session with ID " + id + " not found"));
    }

    public Session createSession(Session session, long tutorId) {
    	logger.info("Creating session for tutor ID: {}", tutorId);
        User tutor = userRepository.findById(tutorId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid tutor ID: " + tutorId));
        session.setTutor(tutor);
        logger.info("Session created for tutor ID: {}", tutorId);
        return sessionRepository.save(session);
    }

    public List<Map<String, Object>> getAllSessionsWithTutorInfo() {
    	logger.info("Fetching all sessions with tutor information");
        List<Session> sessions = getAllSessions();
        List<Map<String, Object>> sessionWithTutorInfoList = new ArrayList<>();

        for (Session session : sessions) {
            Map<String, Object> sessionWithTutorInfo = new HashMap<>();
            sessionWithTutorInfo.put("sessionId", session.getSessionId());
            sessionWithTutorInfo.put("sessionName", session.getSessionName());
            sessionWithTutorInfo.put("sessionDescription", session.getSessionDescription());
            sessionWithTutorInfo.put("startDate", session.getStartDate());
            sessionWithTutorInfo.put("endDate", session.getEndDate());
            sessionWithTutorInfo.put("frequency", session.getFrequency());
            sessionWithTutorInfo.put("duration", session.getDuration());
            sessionWithTutorInfo.put("status", session.getStatus());

            // Fetch tutor information using fk_tutor_id
            User tutor = session.getTutor();
            sessionWithTutorInfo.put("tutorUsername", tutor.getUsername());
            sessionWithTutorInfo.put("tutorEmail", tutor.getEmail());
//            sessionWithTutorInfo.put("tutorDescription", tutor.getDescription());

            sessionWithTutorInfoList.add(sessionWithTutorInfo);
        }

        return sessionWithTutorInfoList;
    }

    // Update
    public Session updateClass(Session session) {
    	logger.info("Updating session with ID: {}", session.getSessionId());
        return sessionRepository.save(session);
    }

    // Delete
    public void deleteClass(Long id) {
    	logger.info("Deleting session with ID: {}", id);
        sessionRepository.deleteById(id);
    }


	public boolean updateSessionStatus(Long sessionId, StatusType newStatus) {
		logger.info("Updating status for session ID: {} to status: {}", sessionId, newStatus);
		Optional<Session> sessionOptional = sessionRepository.findById(sessionId);
		if (sessionOptional.isPresent()) {
            Session session = sessionOptional.get();
            session.setStatus(newStatus);
            sessionRepository.save(session);
            logger.info("Status updated for session ID: {} to status: {}", sessionId, newStatus);
            return true;
		} else {
			logger.warn("Session ID: {} not found for status update", sessionId);
			return false;
		}
	}

	public boolean deleteSession(Long sessionId) {
		logger.info("Attempting to delete session with ID: {}", sessionId);
		if (sessionRepository.existsById(sessionId)) {
            sessionRepository.deleteById(sessionId);
            logger.info("Session with ID: {} deleted", sessionId);
            return true;
		} else {
			logger.warn("Session ID: {} not found for deletion", sessionId);
			return false;
		}
	}

}
