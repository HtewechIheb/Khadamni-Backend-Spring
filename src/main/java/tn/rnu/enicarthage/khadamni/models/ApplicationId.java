package tn.rnu.enicarthage.khadamni.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationId implements Serializable {
    private Long offerId;
    private Long candidateId;
}
