package tn.rnu.enicarthage.khadamni.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import tn.rnu.enicarthage.khadamni.validation.AllowedFileExtensions;
import tn.rnu.enicarthage.khadamni.validation.MaxFileSize;
import tn.rnu.enicarthage.khadamni.models.Company;

import java.io.IOException;
import java.util.Objects;

import static tn.rnu.enicarthage.khadamni.shared.Globals.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompanyDTO {
    public String name;

    public String address;

    public String description;

    public String contactNumber;

    public String category;

    @MaxFileSize(2 * 1024 * 1024)
    @AllowedFileExtensions({ "jpg", "jpeg", "png" })
    public MultipartFile logoFile;

    public Company toModel() throws IOException {
        Company company = new Company();
        company.setName(parseNullableString(name));
        company.setAddress(parseNullableString(address));
        company.setDescription(parseNullableString(description));
        company.setContactNumber(parseNullableString(contactNumber));
        company.setCategory(parseNullableString(category));
        company.setLogoFile(Objects.nonNull(logoFile) ? logoFile.getBytes() : null);
        company.setLogoFileName(Objects.nonNull(logoFile) ? generateFileName(LOGO_PREFIX, logoFile.getOriginalFilename()) : null);

        return company;
    }
}
