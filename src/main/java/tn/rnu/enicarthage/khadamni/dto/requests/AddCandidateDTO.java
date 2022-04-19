package tn.rnu.enicarthage.khadamni.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.internal.util.StringHelper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import tn.rnu.enicarthage.khadamni.validation.AllowedFileExtensions;
import tn.rnu.enicarthage.khadamni.validation.MaxFileSize;
import tn.rnu.enicarthage.khadamni.models.Candidate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDate;

import static tn.rnu.enicarthage.khadamni.shared.Globals.*;
import static tn.rnu.enicarthage.khadamni.shared.Globals.PHOTO_PREFIX;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCandidateDTO {
    @NotBlank
    public String firstName;

    @NotBlank
    public String lastName;

    public String address;

    @NotBlank
    public String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate birthdate;

    @NotNull
    @MaxFileSize(5 * 1024 * 1024)
    @AllowedFileExtensions({ "pdf" })
    public MultipartFile resumeFile;

    @NotNull
    @MaxFileSize(2 * 1024 * 1024)
    @AllowedFileExtensions({ "jpg", "jpeg", "png" })
    public MultipartFile photoFile;

    public Candidate toModel() throws IOException {
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
