package tutorBooking.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import tutorBooking.model.Review;
import tutorBooking.services.ReviewService;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

	@GetMapping
	public List<Review> getReview() {
		return reviewService.getAllReview();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Review> getReview(@PathVariable("id") long reviewId) {
		Review review = reviewService.getReviewById(reviewId);
		return ResponseEntity.ok(review);
	}

    @PostMapping
    public ResponseEntity<Review> saveReview(
            @RequestBody Review review,
            @RequestParam long tutorId,
            @RequestParam long studentId) {
        Review savedReview = reviewService.createReview(review, tutorId, studentId);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedReview.getReviewId()).toUri();
        return ResponseEntity.created(location).body(savedReview);
    }

}