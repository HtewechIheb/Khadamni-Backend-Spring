package tn.rnu.enicarthage.khadamni.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class RefreshToken {
    @Id
    private String token;

    @NotNull
    private String jwtId;

    @NotNull
    private Boolean isRevoked;

    @NotNull
    private LocalDateTime addedDate;

    @NotNull
    private LocalDateTime expiryDate;

    @JsonIgnore
    @ManyToOne(optional = false, cascade = CascadeType.REMOVE)
    private AppUser user;
}
