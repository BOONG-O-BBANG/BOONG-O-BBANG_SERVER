package com.project.boongobbang;

import com.project.boongobbang.domain.entity.roommate.Roommate;
import com.project.boongobbang.domain.entity.user.User;
import com.project.boongobbang.enums.*;
import com.project.boongobbang.repository.roommate.RoommateRepository;
import com.project.boongobbang.repository.user.UserRepository;
import com.project.boongobbang.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@EnableJpaAuditing
@SpringBootApplication
public class BoongobbangApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoongobbangApplication.class, args);
	}

		@Bean
	public CommandLineRunner init(UserService userService,
								  UserRepository userRepository,
								  RoommateRepository roommateRepository) {
		return args -> {
			Random random = new Random();

			for (int i = 1; i <= 100; i++) {
				User user = User.builder()
						.username("user" + i)
						.userNaverId("naverId" + i)
						.userNickname("userNickname" + i)
						.userEmail("user" + i + "@example.com")
						.userBirth(LocalDate.now().minusYears(20 + random.nextInt(30)))  // 20 to 50 years old
						.userMobile(String.format("%03d-%04d-%04d", random.nextInt(1000), random.nextInt(10000), random.nextInt(10000)))

						.userGender(random.nextBoolean() ? Gender.MAN : Gender.WOMAN)
						.userCleanCount(CleanCount.values()[random.nextInt(CleanCount.values().length)])
						.userLocation(SeoulGu.values()[random.nextInt(SeoulGu.values().length)])
						.userMBTI(MBTI.values()[random.nextInt(MBTI.values().length)])
						.role(Role.ROLE_USER)

						.userHasPet(random.nextBoolean())
						.userHasExperience(random.nextBoolean())
						.userIsSmoker(random.nextBoolean())
						.userIsNocturnal(random.nextBoolean())

						.userIntroduction("Hello, I am user" + i)
						.userPhotoUrl("")  // empty for now

						// 기본값
						.ratedCount(0L)
						.averageScore(null)
						.sentRoommateList(new ArrayList<>())
						.receivedRoommateList(new ArrayList<>())
						.receivedNotificationList(new ArrayList<>())
						.gaveScoreList(new ArrayList<>())

						// userType 설정
						.userType(null)  // 이부분은 나중에 setUserType 메서드를 사용해서 설정
						.isPaired(false)
						.build();

				userRepository.save(user);
				user.setUserType(userService.determineUserType(user));
				userRepository.save(user);
			}

			List<User> allUsers = userRepository.findAll();
			int roommateCount = 0; // 예를 들어, 100개의 Roommate 객체를 생성한다고 가정
			Set<User> pairedUsers = new HashSet<>(); // 이미 룸메이트 관계를 맺은 유저들을 저장

			for (int i = 0; i < roommateCount; i++) {
				User user1 = allUsers.get(random.nextInt(allUsers.size()));
				while (pairedUsers.contains(user1)) {
					user1 = allUsers.get(random.nextInt(allUsers.size()));
				}

				User user2 = allUsers.get(random.nextInt(allUsers.size()));
				while (user1.equals(user2) || pairedUsers.contains(user2)) {
					user2 = allUsers.get(random.nextInt(allUsers.size()));
				}

				pairedUsers.add(user1);
				pairedUsers.add(user2);

				user1.setIsPaired(true);
				userRepository.save(user1);
				user2.setIsPaired(true);
				userRepository.save(user2);

				Roommate roommate = Roommate.builder()
						.user1(user1)
						.user2(user2)
						.build();

				roommate.start(); // 시작 날짜 설정

				roommateRepository.save(roommate); // Roommate 객체를 저장
			}
		};
	}

}
