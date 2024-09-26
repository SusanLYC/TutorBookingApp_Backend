package tutorBooking.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "review")
public class Review {

	@Id
	@GeneratedValue()
	private long reviewId;

	@Size(min = 1, max = 5, message = "Please rate between 1 and 5, where 1 is the lowest and 5 is the highest.")
	private long rating;
	private LocalDateTime reviewTime = LocalDateTime.now();

	private String review;


	// Relation: Review and User(student)
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "FK_STUDENT_ID", nullable = false)
	private User student;

	// Relation: Review and User(tutor)
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "FK_TUTOR_ID", nullable = false)
	private User tutor;


	public User getStudent() {
		return student;
	}

	public User getTutor() {
		return tutor;
	}

	// No-args constructor
	public Review() {
	}

	// Constructor
	public Review(long rating, String review) {
		this.rating = rating;
		this.reviewTime = LocalDateTime.now();
		this.review = review;
	}

	// Auto-generated getters and setters

	public long getReviewId() {
		return reviewId;
	}

	public void setReviewId(long reviewId) {
		this.reviewId = reviewId;
	}

	public long getRating() {
		return rating;
	}

	public void setRating(long rating) {
		this.rating = rating;
	}

	public LocalDateTime getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(LocalDateTime reviewTime) {
		this.reviewTime = reviewTime;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public void setTutor(User tutor) {
		this.tutor = tutor;
	}


}
