package tn.rnu.enicarthage.khadamni.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import tn.rnu.enicarthage.khadamni.models.Company;
import tn.rnu.enicarthage.khadamni.validation.AllowedFileExtensions;
import tn.rnu.enicarthage.khadamni.validation.MaxFileSize;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.io.IOException;

import static tn.rnu.enicarthage.khadamni.shared.Globals.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCompanyDTO {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    private String description;

    private String contactNumber;

    private String category;

    @NotNull
    @MaxFileSize(2 * 1024 * 1024)
    @AllowedFileExtensions({ "jpg", "jpeg", "png" })
    private MultipartFile logoFile;

    public Company getCompany() throws IOException {
        Company company = new Company();
        company.setName(name);
        company.setAddress(address);
        company.setDescription(description);
        company.setContactNumber(parseNullableString(contactNumber));
        company.setCategory(parseNullableString(category));
        company.setLogoFile(logoFile.getBytes());
        company.setLogoFileName(generateFileName(LOGO_PREFIX, logoFile.getOriginalFilename()));

        return company;
    }
}
