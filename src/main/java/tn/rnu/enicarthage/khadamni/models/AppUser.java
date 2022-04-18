package tn.rnu.enicarthage.khadamni.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.rnu.enicarthage.khadamni.shared.enumerations.UserType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String userName;

    @NotNull
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
}
