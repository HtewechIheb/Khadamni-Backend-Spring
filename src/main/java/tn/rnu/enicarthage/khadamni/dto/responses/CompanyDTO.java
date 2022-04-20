package tn.rnu.enicarthage.khadamni.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.rnu.enicarthage.khadamni.models.Company;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {
    private Long id;
    private String name;
    private String address;
    private String description;
    private String contactNumber;
    private String category;

    public static CompanyDTO fromModel(Company company) {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(company.getId());
        companyDTO.setName(company.getName());
        companyDTO.setAddress(company.getAddress());
        companyDTO.setDescription(company.getDescription());
        companyDTO.setContactNumber(company.getContactNumber());
        companyDTO.setCategory(company.getCategory());

        return companyDTO;
    }
}
