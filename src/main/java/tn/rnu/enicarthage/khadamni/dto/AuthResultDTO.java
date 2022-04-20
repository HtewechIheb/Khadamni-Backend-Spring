package tn.rnu.enicarthage.khadamni.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.rnu.enicarthage.khadamni.models.AppUser;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResultDTO {
    private AppUser user;
    private String token;
    private String refreshToken;
    private boolean succeeded;
    private String error;
}
