package tutorBooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import tutorBooking.security.RsaKeyProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class) // Enable RsaKeyProperties
@EnableJpaAuditing
@EnableWebSecurity
@EnableMethodSecurity
public class TutorBookingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TutorBookingAppApplication.class, args);
	}

}
