package tn.rnu.enicarthage.khadamni.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.rnu.enicarthage.khadamni.models.Candidate;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateDTO {
    public Long id;
    public String firstName;
    public String lastName;
    public String address;
    public String gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate birthdate;

    public static CandidateDTO fromModel(Candidate candidate) {
        CandidateDTO candidateDTO = new CandidateDTO();
        candidateDTO.setId(candidate.getId());
        candidateDTO.setFirstName(candidate.getFirstName());
        candidateDTO.setLastName(candidate.getLastName());
        candidateDTO.setGender(candidate.getGender());
        candidateDTO.setAddress(candidate.getAddress());
        candidateDTO.setBirthdate(candidate.getBirthdate());

        return candidateDTO;
    }
}
