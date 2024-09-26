package tutorBooking.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import tutorBooking.enums.RoleType;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private long userId;

	@NotNull(message = "Username cannot be null.")
	@Column(unique = true, nullable = false)
	private String username;

	@NotNull(message = "Password cannot be null.")
	@Column(nullable = false)
	private String password;

	@Email(message = "Please provide a valid email.")
	@Column(unique = true, nullable = false)
	private String email;

	@NotNull(message = "Role cannot be null.")
	@Enumerated(value = EnumType.STRING)
	private RoleType role;

	private boolean deleted = false; // allow soft delete, as the associated review are not supposed to be deleted

	// Relation: Student and class
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "STUDENT_SESSION", joinColumns = @JoinColumn(name = "FK_STUDENT_ID"), inverseJoinColumns = @JoinColumn(name = "FK_CLASS_ID"))
	private List<Session> enrolledClasses = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "tutor")
	private List<Session> ownedClasses = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "student")
	private List<Review> writtenReviews = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "tutor")
	private List<Review> receivedReviews = new ArrayList<>();

	// No-args constructor
	public User() {
	}

	// Constructor
	public User(String username, String password, String email, RoleType role) {
		this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
	}

	// Auto-generated getters and setters
	public long getUserId() {
		return userId;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public RoleType getRole() {
		return role;
	}

	public void setRole(RoleType role) {
		this.role = role;
	}

	public List<Session> getEnrolledClasses() {
		return enrolledClasses;
	}

	public void setEnrolledClasses(List<Session> enrolledClasses) {
		this.enrolledClasses = enrolledClasses;
	}

	public List<Session> getOwnedClasses() {
		return ownedClasses;
	}

	public void setOwnedClasses(List<Session> ownedClasses) {
		this.ownedClasses = ownedClasses;
	}

	public List<Review> getWrittenReviews() {
		return writtenReviews;
	}

	public void setWrittenReviews(List<Review> writtenReviews) {
		this.writtenReviews = writtenReviews;
	}

	public List<Review> getReceivedReviews() {
		return receivedReviews;
	}

	public void setReceivedReviews(List<Review> receivedReviews) {
		this.receivedReviews = receivedReviews;
	}

}