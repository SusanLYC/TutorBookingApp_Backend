package tutorBooking.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import tutorBooking.enums.StatusType;
import tutorBooking.model.Session;
import tutorBooking.model.User;
import tutorBooking.services.SessionService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/session")
public class SessionController {

    private static final Logger logger = LoggerFactory.getLogger(SessionController.class);

    @Autowired
    private SessionService sessionService;

    // Get sessions for a specific user
//    @GetMapping("/users/{userId}/sessions")
//    public ResponseEntity<List<Session>> getUserEnrolledOrOwnedSessions(@PathVariable long userId) {
//        logger.info("Fetching sessions for user with ID: {}", userId);
//    	List<Session> sessions = sessionService.getUserEnrolledOrOwnedSessions(userId);
//        return ResponseEntity.ok(sessions);
//    }
    @GetMapping("/users/{userId}/sessions")
    public ResponseEntity<List<Map<String, Object>>> getUserEnrolledOrOwnedSessions(@PathVariable long userId) {
        logger.info("Fetching sessions for user with ID: {}", userId);

        // Get the sessions for the user
        List<Session> sessions = sessionService.getUserEnrolledOrOwnedSessions(userId);
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
            if (tutor != null) {
                sessionWithTutorInfo.put("tutorUsername", tutor.getUsername());
                sessionWithTutorInfo.put("tutorEmail", tutor.getEmail());
//                sessionWithTutorInfo.put("tutorDescription", tutor.getDescription());
            }

            sessionWithTutorInfoList.add(sessionWithTutorInfo);
        }

        return ResponseEntity.ok(sessionWithTutorInfoList);
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getSessions() {
        logger.info("Fetching all sessions with tutor info");
    	List<Map<String, Object>> sessions = sessionService.getAllSessionsWithTutorInfo();
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getSession(@PathVariable("id") long sessionId) {
        logger.info("Fetching session with ID: {}", sessionId);
    	Optional<Session> session = Optional.of(sessionService.getSessionById(sessionId));
        return session.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Session with ID {} not found", sessionId);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping("/createcourse")
    public ResponseEntity<Session> saveSession(
            @RequestBody Session session,
            @RequestParam long tutorId) {
        logger.info("Creating session with tutor ID: {}", tutorId);
        try {
            // Validate tutorId and session fields
            Session savedSession = sessionService.createSession(session, tutorId);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(savedSession.getSessionId()).toUri();
            return ResponseEntity.created(location).body(savedSession);
        } catch (Exception e) {
            logger.error("Error creating session", e);
            return ResponseEntity.badRequest().body(null); // Return a more descriptive error message if needed
        }
    }

    @PutMapping("/{sessionId}/updatestatus")
    public ResponseEntity<String> updateSessionStatus(@PathVariable Long sessionId, @RequestBody Map<String, String> request) {
        logger.info("Updating status for session ID: {} to status: {}", sessionId, request.get("status"));
    	try {
            StatusType newStatus = StatusType.valueOf(request.get("status").toUpperCase());
            boolean isUpdated = sessionService.updateSessionStatus(sessionId, newStatus);

            if (isUpdated) {
                return ResponseEntity.ok("Status updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update status");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid status value: {}", request.get("status"), e);
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status value");
        }
    }

    @DeleteMapping("/{sessionId}/delete")
    public ResponseEntity<String> deleteSession(@PathVariable Long sessionId) {
        logger.info("Deleting session with ID: {}", sessionId);
    	boolean isDeleted = sessionService.deleteSession(sessionId);
        if (isDeleted) {
            return ResponseEntity.ok("Course deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
    }
}
