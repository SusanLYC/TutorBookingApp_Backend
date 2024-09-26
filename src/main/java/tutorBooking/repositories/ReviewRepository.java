package tutorBooking.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tutorBooking.model.Review;
import tutorBooking.model.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{

	List<Review> findByStudent(User student);
    List<Review> findByTutor(User tutor);

}
