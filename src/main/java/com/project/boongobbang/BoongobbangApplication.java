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
			List<String> lastNames = Arrays.asList("김", "이", "박", "최", "정", "강", "조", "윤", "장", "임", "한", "오", "서", "신", "권", "황", "안", "송", "류", "전", "홍", "고", "문", "양", "손", "배", "조", "백", "허", "유", "남", "심", "노", "하", "곽", "성", "차", "주", "우", "구");
			List<String> maleNames = Arrays.asList("은기", "현욱", "상혁", "준호", "민수", "성민", "동욱", "재현", "석진", "영호", "동현", "준영", "선호", "태현", "승민", "대성", "민재", "하준", "진우", "재욱", "수환", "태윤", "동하", "석희", "우진", "호진", "성훈", "재훈", "시우", "종민", "예준", "준서", "지후", "승우", "유준", "준혁", "준희", "찬우", "현준", "유찬", "시훈", "지훈", "도윤", "건우", "서준", "현우", "윤우", "동준", "태민", "서진", "준수", "승현", "우재", "승재", "민찬", "유성", "재윤", "민규", "준", "윤재", "예성", "승준", "준상", "현", "건", "동건", "준석", "석우", "승훈", "지현", "도현", "성욱", "기현", "우영", "재민", "찬민", "태진", "성윤", "성진", "예찬", "영석", "준명", "수혁", "태영", "유민", "찬혁", "태환", "유환", "시윤", "성재", "윤호", "건호", "성호", "도훈", "기훈", "민호", "유현", "찬호", "현서", "우석", "윤석", "기준", "예준", "준기", "지운", "서우", "승윤", "지환", "건후", "상우", "상훈", "민욱", "기욱", "유하", "도하", "재석", "수진", "유진", "기영", "상윤", "민영", "준영", "태규", "우규", "승규", "진규", "도규");
			List<String> femaleNames = Arrays.asList("연주", "지현", "혜진", "서연", "민지", "소영", "영진", "지은", "하린", "은서", "소윤", "수빈", "지아", "하은", "서아", "승아", "유진", "수민", "예은", "예린", "서진", "하연", "수연", "예지", "주아", "은미", "수아", "선미", "은지", "영서", "예빈", "유나", "은영", "서영", "하나", "민아", "지영", "윤서", "서희", "지유", "지안", "하윤", "수진", "예나", "민영", "유빈", "유림", "선영", "민경", "유경", "지수", "수영", "하영", "서은", "은아", "서율", "영은", "유미", "수희", "예서", "하진", "유정", "하정", "영화", "선아", "지민", "하민", "유린", "은빈", "예영", "은유", "예미", "수라", "하라", "서하", "예하", "지라", "하늘", "하라", "민라", "소라", "지희", "서희", "하희", "예희", "민희", "유하", "소하", "하솔", "지솔", "서솔", "은솔", "예솔", "하람", "소람", "지람", "유람", "예람", "승람", "민람", "은람");

			for (int i = 1; i <= 100; i++) {

				User user = User.builder()
						.username("")
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
						.userPhotoUrl("")

						// 기본값
						.ratedCount(0L)
						.averageScore(null)
//						.sentRoommateList(new ArrayList<>())
//						.receivedRoommateList(new ArrayList<>())
						.receivedNotificationList(new ArrayList<>())
//						.gaveScoreList(new ArrayList<>())

						// userType 설정
						.userType(null)  // 이부분은 나중에 setUserType 메서드를 사용해서 설정
						.isPaired(false)
						.build();

				String userLastName = lastNames.get(random.nextInt(lastNames.size()));
				String userFirstName;
				if (user.getUserGender() == Gender.MAN) {
					userFirstName = maleNames.get(random.nextInt(maleNames.size()));
				} else {
					userFirstName = femaleNames.get(random.nextInt(femaleNames.size()));
				}
				String fullUserName = userLastName + userFirstName;
				user.setUsername(fullUserName);
				userRepository.save(user);

				user.setUserType(userService.determineUserType(user));
				userRepository.save(user);
			}

//			List<User> allUsers = userRepository.findAll();
//			int roommateCount = 10; // 예를 들어, n개의 Roommate 객체를 생성한다고 가정
//			Set<User> pairedUsers = new HashSet<>(); // 이미 룸메이트 관계를 맺은 유저들을 저장
//
//			for (int i = 0; i < roommateCount; i++) {
//				User user1 = allUsers.get(random.nextInt(allUsers.size()));
//				while (pairedUsers.contains(user1)) {
//					user1 = allUsers.get(random.nextInt(allUsers.size()));
//				}
//
//				User user2 = allUsers.get(random.nextInt(allUsers.size()));
//				while (user1.equals(user2) || pairedUsers.contains(user2)) {
//					user2 = allUsers.get(random.nextInt(allUsers.size()));
//				}
//
//				pairedUsers.add(user1);
//				pairedUsers.add(user2);
//
//				user1.setIsPaired(true);
//				userRepository.save(user1);
//				user2.setIsPaired(true);
//				userRepository.save(user2);
//
//				Roommate roommate = Roommate.builder()
//						.user1(user1)
//						.user2(user2)
//						.build();
//
//				roommate.start(); // 시작 날짜 설정
//
//				roommateRepository.save(roommate); // Roommate 객체를 저장
//			}
		};
	}

}
