package prm.projectbase;

import prm.projectbase.entity.Role;
import prm.projectbase.entity.User;
import prm.projectbase.repository.RoleRepository;
import prm.projectbase.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// Admin credentials: override via environment variables in production.
	//   ADMIN_USERNAME, ADMIN_PASSWORD, ADMIN_EMAIL
	@Value("${ADMIN_SEED_ENABLED:false}")
	private boolean adminSeedEnabled;

	@Value("${ADMIN_USERNAME:gymtl_admin_2025}")
	private String adminUsername;

	@Value("${ADMIN_PASSWORD:}")
	private String adminPassword;

	@Value("${ADMIN_EMAIL:gymtelligent.admin@gmail.com}")
	private String adminEmail;

	@Bean
	public CommandLineRunner databaseInitializer(
			RoleRepository roleRepository,
			UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {
			if (roleRepository.count() == 0) {
				// 1. Initialize Roles
				Role adminRole = roleRepository.save(Role.builder()
						.name("ROLE_ADMIN")
						.description("Administrator with full system privileges")
						.build());

				roleRepository.save(Role.builder()
						.name("ROLE_USER")
						.description("Standard User with restricted read access")
						.build());

				if (adminSeedEnabled) {
					if (adminPassword == null || adminPassword.isBlank()) {
						throw new IllegalStateException("ADMIN_PASSWORD must be set when ADMIN_SEED_ENABLED=true");
					}

					userRepository.save(User.builder()
							.userName(adminUsername)
							.password(passwordEncoder.encode(adminPassword))
							.email(adminEmail)
							.fullName("System Administrator")
							.active(true)
							.role(adminRole)
							.build());
				}
			}
		};
	}
}
