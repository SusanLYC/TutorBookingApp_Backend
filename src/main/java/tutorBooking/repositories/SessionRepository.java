package tutorBooking.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tutorBooking.model.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long>{

    List<Session> findByStudents_UserId(long userId);

    List<Session> findByTutor_UserId(long userId);

}
