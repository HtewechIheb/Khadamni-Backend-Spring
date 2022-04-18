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
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    @EmbeddedId
    private ApplicationId applicationId;

    @NotNull
    private LocalDateTime date;

    @NotNull
    private String status;

    @JsonIgnore
    @MapsId("offerId")
    @ManyToOne(optional = false)
    private Offer offer;

    @JsonIgnore
    @MapsId("candidateId")
    @ManyToOne(optional = false)
    private Candidate candidate;
}
