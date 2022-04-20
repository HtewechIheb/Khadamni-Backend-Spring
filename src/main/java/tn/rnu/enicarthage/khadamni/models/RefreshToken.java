package tn.rnu.enicarthage.khadamni.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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
