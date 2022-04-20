package tn.rnu.enicarthage.khadamni.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import tn.rnu.enicarthage.khadamni.shared.enumerations.UserType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String userName;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private UserType type;

    @JsonIgnore
    @NotNull
    private String passwordHash;

    @JsonIgnore
    @OneToOne(mappedBy = "account")
    private Company company;

    @JsonIgnore
    @OneToOne(mappedBy = "account")
    private Candidate candidate;

    public UserDetails getUserDetails() {
        return new User(userName, passwordHash, new ArrayList<>());
    }
}
