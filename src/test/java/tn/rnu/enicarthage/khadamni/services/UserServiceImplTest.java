package tn.rnu.enicarthage.khadamni.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import tn.rnu.enicarthage.khadamni.models.AppUser;
import tn.rnu.enicarthage.khadamni.repositories.UserRepository;
import tn.rnu.enicarthage.khadamni.services.interfaces.UserService;
import tn.rnu.enicarthage.khadamni.shared.enumerations.UserType;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @Test
    @DisplayName("getById(): Should return the correct user when a valid id is provided.")
    void whenGetById_withValidId_thenReturnValidUser() {
        AppUser appUser = new AppUser(1L, "test@test.com", "test@test.com", UserType.Company, "PASSWORD_HASH", null, null);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(appUser));

        AppUser appUserToTest = userService.getById(1L);
        assertEquals(appUserToTest, appUser);
    }

    @Test
    @DisplayName("getById(): Should return null when an invalid id is provided.")
    void whenGetById_withInvalidId_thenReturnNull() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        AppUser appUserToTest = userService.getById(1L);
        assertNull(appUserToTest);
    }

    @Test
    @DisplayName("findByUserName(): Should return the correct user when a valid username is provided.")
    void whenFindByUserName_withValidUserName_thenReturnValidUser() {
        AppUser appUser = new AppUser(1L, "test@test.com", "test@test.com", UserType.Company, "PASSWORD_HASH", null, null);
        Mockito.when(userRepository.findByUserName("test@test.com")).thenReturn(Optional.of(appUser));

        AppUser appUserToTest = userService.findByUserName("test@test.com");
        assertEquals(appUserToTest, appUser);
    }

    @Test
    @DisplayName("findByUserName(): Should return null when an invalid username is provided.")
    void whenFindByUserName_withInvalidUserName_thenReturnNull() {
        Mockito.when(userRepository.findByUserName("test@test.com")).thenReturn(Optional.empty());

        AppUser appUserToTest = userService.findByUserName("test@test.com");
        assertNull(appUserToTest);
    }

    @Test
    @DisplayName("findByEmail(): Should return the correct user when a valid email is provided.")
    void whenFindByEmail_withValidEmail_thenReturnValidUser() {
        AppUser appUser = new AppUser(1L, "test@test.com", "test@test.com", UserType.Company, "PASSWORD_HASH", null, null);
        Mockito.when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(appUser));

        AppUser appUserToTest = userService.findByEmail("test@test.com");
        assertEquals(appUserToTest, appUser);
    }

    @Test
    @DisplayName("findByEmail(): Should return null when an invalid email is provided.")
    void whenFindByEmail_withInvalidEmail_thenReturnNull() {
        Mockito.when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        AppUser appUserToTest = userService.findByEmail("test@test.com");
        assertNull(appUserToTest);
    }

    @Test
    @DisplayName("addUser(): Should set password hash and return added user.")
    void whenAddUser_thenSetPasswordHashAndReturnAddedUser() {
        String userPassword = "user_password";
        String userPasswordHash = "PASSWORD_HASH";
        AppUser appUser = new AppUser(1L, "test@test.com", "test@test.com", UserType.Company, null, null, null);

        Mockito.when(passwordEncoder.encode(userPassword)).thenReturn(userPasswordHash);
        Mockito.when(userRepository.save(appUser)).thenReturn(appUser);

        AppUser appUserToTest = userService.addUser(appUser, userPassword);

        assertEquals(appUser.getPasswordHash(), userPasswordHash);
        Mockito.verify(userRepository, Mockito.times(1)).save(appUser);
        assertEquals(appUserToTest, appUser);
    }

    @Test
    @DisplayName("checkPassword(): Should return true when the correct password is provided.")
    void whenCheckPassword_withCorrectPassword_thenReturnTrue() {
        String userPassword = "user_password";
        AppUser appUser = new AppUser(1L, "test@test.com", "test@test.com", UserType.Company, "PASSWORD_HASH", null, null);

        Mockito.when(passwordEncoder.matches(userPassword, appUser.getPasswordHash())).thenReturn(true);
        boolean result = userService.checkPassword(appUser, userPassword);

        assertTrue(result);
    }

    @Test
    @DisplayName("checkPassword(): Should return false when an incorrect password is provided.")
    void whenCheckPassword_withIncorrectPassword_thenReturnFalse() {
        String userPassword = "user_password";
        AppUser appUser = new AppUser(1L, "test@test.com", "test@test.com", UserType.Company, "PASSWORD_HASH", null, null);

        Mockito.when(passwordEncoder.matches(userPassword, appUser.getPasswordHash())).thenReturn(false);
        boolean result = userService.checkPassword(appUser, userPassword);

        assertFalse(result);
    }
}