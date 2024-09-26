package tutorBooking.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tutorBooking.exception.ResourceNotFoundException;
import tutorBooking.model.Review;
import tutorBooking.model.User;
import tutorBooking.repositories.ReviewRepository;
import tutorBooking.repositories.UserRepository;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;
	private UserRepository userRepository;

	// Constructor
	public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository) {
		this.reviewRepository = reviewRepository;
		this.userRepository = userRepository;
	}

	public List<Review> getAllReview() {
        return reviewRepository.findAll();
    }

	// create
    public Review createReview(Review review, long tutorId, long studentId) {
        User tutor = userRepository.findById(tutorId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid tutor ID: " + tutorId));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid student ID: " + studentId));
        review.setTutor(tutor);
        review.setStudent(student);
        return reviewRepository.save(review);
    }

	// Read
	public Review getReviewById(Long id) {
	    return reviewRepository.findById(id).orElse(null);
	}

	// Update
	public void updateReview(Review review) {
		reviewRepository.save(review);
	}

	// Delete
	public void deleteReview(Long id) {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isPresent()) {
            reviewRepository.deleteById(id);
        } else {
            System.out.println("Review with ID " + id + " does not exist.");
        }
    }

    public List<Review> findReviewsByStudent(User student) {
        return reviewRepository.findByStudent(student);
    }

    public List<Review> findReviewsByTutor(User tutor) {
        return reviewRepository.findByTutor(tutor);
    }

}
