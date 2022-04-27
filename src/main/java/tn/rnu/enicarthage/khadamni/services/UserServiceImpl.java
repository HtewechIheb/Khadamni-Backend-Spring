package tn.rnu.enicarthage.khadamni.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.rnu.enicarthage.khadamni.models.AppUser;
import tn.rnu.enicarthage.khadamni.repositories.UserRepository;
import tn.rnu.enicarthage.khadamni.services.interfaces.UserService;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AppUser getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public AppUser findByUserName(String userName) {
        return userRepository.findByUserName(userName).orElse(null);
    }

    @Override
    public AppUser findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public AppUser addUser(AppUser user, String password) {
        user.setPasswordHash(passwordEncoder.encode(password));

        return userRepository.save(user);
    }

    @Override
    public boolean checkPassword(AppUser appUser, String password) {
        return passwordEncoder.matches(password, appUser.getPasswordHash());
    }
}
