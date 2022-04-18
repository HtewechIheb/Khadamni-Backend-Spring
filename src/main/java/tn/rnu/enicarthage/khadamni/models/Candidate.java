package tn.rnu.enicarthage.khadamni.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String gender;

    private String address;

    private LocalDate birthdate;

    @JsonIgnore
    @NotNull
    @Lob
    private byte[] resumeFile;

    @JsonIgnore
    @NotNull
    private String resumeFileName;

    @JsonIgnore
    @NotNull
    @Lob
    private byte[] photoFile;

    @JsonIgnore
    @NotNull
    private String photoFileName;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private AppUser account;

    @JsonIgnore
    @OneToMany(mappedBy = "candidate")
    private List<Application> applications;
}
