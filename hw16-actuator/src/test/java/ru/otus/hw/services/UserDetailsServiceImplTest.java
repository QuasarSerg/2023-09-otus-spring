package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = {UserRepository.class, UserDetailsServiceImpl.class})
class UserDetailsServiceImplTest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userService;

    @Test
    void loadUserByUsername() {
        Mockito.when(userRepository.findByUsername(any(String.class)))
                .thenReturn(Optional.of(new User(1L, "TestUser", "TestPassword", "TestRole")));

        assertThat(userService.loadUserByUsername("TestUser")).isNotNull()
                .matches(user -> user.getUsername().equals("TestUser"))
                .matches(user -> user.getPassword().equals("TestPassword"));
    }
}