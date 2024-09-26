package tutorBooking.security;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tutorBooking.exception.RecordNotFoundException;
import tutorBooking.model.User;
import tutorBooking.model.dto.LoginResponseDTO;
import tutorBooking.model.dto.UserDetailsDTO;
import tutorBooking.repositories.UserRepository;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	Logger LOG = LoggerFactory.getLogger(AuthController.class);

	private final TokenService tokenService;
	private final UserRepository userRepo;

	public AuthController(TokenService tokenService, UserRepository userRepo) {
		this.tokenService = tokenService;
		this.userRepo = userRepo;
	}

	@PostMapping("/login")
	public LoginResponseDTO token(Authentication authentication) throws BadRequestException {
		if (authentication == null) {
			LOG.debug("Token requested for an anonymous user");
			throw new BadRequestException("Authorization header is missing or invalid");
		}

		User user = userRepo.findByUsername(authentication.getName()).orElseThrow(
				() -> new RecordNotFoundException("User not found"));

		UserDetailsDTO userDetailsDTO = new UserDetailsDTO(user);

		LOG.debug("Token requested for user: '{}'", authentication.getName());
		String token = tokenService.generateToken(authentication);
		LOG.debug("Token granted for user '{}': {}", authentication.getName(), token);
		System.out.println("Token generated");

		return new LoginResponseDTO(userDetailsDTO, token);
	}

}
