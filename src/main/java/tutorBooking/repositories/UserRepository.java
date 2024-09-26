package tutorBooking.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tutorBooking.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByUsername(String username);

	Optional<User> findByUserId(Long userId);

	@Query("SELECT u FROM User u WHERE u.deleted = false")
	List<User> findByDeletedFalse();

	boolean existsByEmail(String email);
    User findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.email = :email WHERE u.userId = :userId")
    void updateEmail(@Param("userId") long userId, @Param("email") String email);

}
