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

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

import static tn.rnu.enicarthage.khadamni.shared.Globals.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCandidateDTO {
    public String firstName;

    public String lastName;

    public String address;

    public String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate birthdate;

    @MaxFileSize(5 * 1024 * 1024)
    @AllowedFileExtensions({ "pdf" })
    public MultipartFile resumeFile;

    @MaxFileSize(2 * 1024 * 1024)
    @AllowedFileExtensions({ "jpg", "jpeg", "png" })
    public MultipartFile photoFile;

    public Candidate toModel() throws IOException {
        Candidate candidate = new Candidate();
        candidate.setFirstName(parseNullableString(firstName));
        candidate.setLastName(parseNullableString(lastName));
        candidate.setAddress(parseNullableString(address));
        candidate.setGender(parseNullableString(gender));
        candidate.setBirthdate(birthdate);
        candidate.setResumeFile(Objects.nonNull(resumeFile) ? resumeFile.getBytes() : null);
        candidate.setResumeFileName(Objects.nonNull(resumeFile) ? generateFileName(RESUME_PREFIX, resumeFile.getOriginalFilename()) : null);
        candidate.setPhotoFile(Objects.nonNull(photoFile) ? photoFile.getBytes() : null);
        candidate.setPhotoFileName(Objects.nonNull(photoFile) ? generateFileName(PHOTO_PREFIX, photoFile.getOriginalFilename()) : null);

        return candidate;
    }
}
