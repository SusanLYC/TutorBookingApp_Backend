package tutorBooking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import tutorBooking.repositories.ReviewRepository;
import tutorBooking.repositories.SessionRepository;
import tutorBooking.repositories.UserRepository;
import tutorBooking.services.ReviewService;
import tutorBooking.services.SessionService;
import tutorBooking.services.UserService;

@Component
public class DataLoader implements ApplicationRunner{

    private SessionRepository sessionRepository;
    private UserRepository userRepository;
    private ReviewRepository reviewRepository;
	private UserService userService;
	private ReviewService reviewService;
	private SessionService sessionService;

    @Autowired
    public DataLoader(SessionRepository sessionRepository, UserRepository userRepository,
                      ReviewRepository reviewRepository,
                      UserService userService, ReviewService reviewService,
                      SessionService sessionService) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.reviewService = reviewService;
        this.sessionService = sessionService;
    }

	@Override
	public void run(ApplicationArguments args) throws Exception {
//        // Test add user
//        User student = new User("student1", "password", "student1@example.com", RoleType.STUDENT);
//        User tutor = new User("tutor1", "password", "tutor1@example.com", RoleType.TUTOR);
//
//        userService.createUser(student);
//        userService.createUser(tutor);
//        userService.updateUserEmail(userService.getUserByName("student1"), "student1@12345.com");
//        Session session = new Session("Cantonese", "Cantonese for beginners", LocalDateTime.now(), LocalDateTime.of(2024, 7, 31, 14, 0, 0), StatusType.AVALIABLE);
//        session.setTutor(tutor);
//        sessionService.createSession(session);
//

//        Review review = new Review();
//        review.setStudent(userRepository.findById((long) 1).get());
//        review.setTutor(userRepository.findById((long) 2).get());
//        review.setReview("Great tutor!");
//        reviewRepository.save(review);

//        userRepository.deleteById((long) 4);
//		  userRepository.deleteById((long) 5);
//        userRepository.deleteById((long) 6);
//        userRepository.deleteById((long) 7);

	}
}
