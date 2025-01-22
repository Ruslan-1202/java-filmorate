package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class})
class FilmoRateApplicationTests {

	private final UserDbStorage userStorage;

	@Test
	public void testFindUserById() {
		User user = new User();
		user.setName("Name 1");
		user.setEmail("email 1");
		user.setBirthday(LocalDate.of(1990, 1, 1));
		userStorage.create(user);

		Optional<User> userOptional = userStorage.get(1L);

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(a ->
						assertThat(a).hasFieldOrPropertyWithValue("id", 1L)
				)
				.hasValueSatisfying(a ->
						assertThat(a).hasFieldOrPropertyWithValue("name", "Name 1")
				);
	}
}