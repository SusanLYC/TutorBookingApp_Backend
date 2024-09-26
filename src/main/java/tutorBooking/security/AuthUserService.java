package tutorBooking.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import tutorBooking.model.User;
import tutorBooking.repositories.UserRepository;

@Service
public class AuthUserService implements UserDetailsService{

	private UserRepository userRepo;

	public AuthUserService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return new AuthUser(user);
	}

	public User getAuthUser(String username) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = ((Jwt) authentication.getPrincipal()).getClaims().get("userId").toString();
		User user = userRepo.findByUserId(Long.parseLong(userId))
				.orElseThrow(() -> new AccessDeniedException(
						"Your authentication token is storing invalid user data, please login again."
								+ " If problem persists, contact support."));
		return user;
	}

}
