package tn.rnu.enicarthage.khadamni.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import tn.rnu.enicarthage.khadamni.validation.AllowedFileExtensions;
import tn.rnu.enicarthage.khadamni.validation.MaxFileSize;
import tn.rnu.enicarthage.khadamni.models.Company;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.io.IOException;

import static tn.rnu.enicarthage.khadamni.shared.Globals.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCompanyDTO {
    @NotBlank
    public String name;

    @NotBlank
    public String address;

    @NotBlank
    public String description;

    public String contactNumber;

    public String category;

    @NotNull
    @MaxFileSize(2 * 1024 * 1024)
    @AllowedFileExtensions({ "jpg", "jpeg", "png" })
    public MultipartFile logoFile;

    public Company toModel() throws IOException {
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
