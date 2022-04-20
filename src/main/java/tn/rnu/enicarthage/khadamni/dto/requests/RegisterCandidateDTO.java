package tn.rnu.enicarthage.khadamni.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import tn.rnu.enicarthage.khadamni.models.Candidate;
import tn.rnu.enicarthage.khadamni.models.Company;
import tn.rnu.enicarthage.khadamni.validation.AllowedFileExtensions;
import tn.rnu.enicarthage.khadamni.validation.MaxFileSize;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static tn.rnu.enicarthage.khadamni.shared.Globals.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCandidateDTO {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String address;

    @NotBlank
    private String gender;

    @NotNull
    @MaxFileSize(value = 5 * 1024 * 1024)
    @AllowedFileExtensions(value = { "pdf" })
    private MultipartFile resumeFile;

    @NotNull
    @MaxFileSize(value = 2 * 1024 * 1024)
    @AllowedFileExtensions(value = { "jpg", "jpeg", "png" })
    private MultipartFile photoFile;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    public Candidate getCandidate() throws IOException {
        Candidate candidate = new Candidate();
        candidate.setFirstName(firstName);
        candidate.setLastName(lastName);
        candidate.setAddress(parseNullableString(address));
        candidate.setGender(gender);
        candidate.setBirthdate(birthdate);
        candidate.setResumeFile(resumeFile.getBytes());
        candidate.setResumeFileName(generateFileName(RESUME_PREFIX, resumeFile.getOriginalFilename()));
        candidate.setPhotoFile(photoFile.getBytes());
        candidate.setPhotoFileName(generateFileName(PHOTO_PREFIX, photoFile.getOriginalFilename()));

        return candidate;
    }
}
