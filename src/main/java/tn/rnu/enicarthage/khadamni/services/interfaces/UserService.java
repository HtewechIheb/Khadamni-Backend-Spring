package tn.rnu.enicarthage.khadamni.services.interfaces;

import tn.rnu.enicarthage.khadamni.models.AppUser;

public interface UserService {
    AppUser findByUserName(String userName);
    AppUser findByEmail(String email);
    AppUser addUser(AppUser appUser, String password);
    boolean checkPassword(AppUser appUser, String password);
}
