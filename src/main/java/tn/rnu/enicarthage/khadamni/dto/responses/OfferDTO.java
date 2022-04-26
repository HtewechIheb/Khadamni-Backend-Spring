package tn.rnu.enicarthage.khadamni.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.rnu.enicarthage.khadamni.models.Offer;

import java.util.Arrays;

import static tn.rnu.enicarthage.khadamni.shared.Globals.parseNullableString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferDTO {
    private Long id;
    private String industry;
    private String title;
    private String description;
    private Integer spots;
    private Double salary;
    private String degree;
    private String gender;
    private String[] skills;
    private String type;
    private String minimumExperience;
    private String recommendedExperience;
    private Long companyId;

    public static OfferDTO fromModel(Offer offer) {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(offer.getId());
        offerDTO.setIndustry(offer.getIndustry());
        offerDTO.setTitle(offer.getTitle());
        offerDTO.setDescription(offer.getDescription());
        offerDTO.setSpots(offer.getSpots());
        offerDTO.setSalary(offer.getSalary());
        offerDTO.setDegree(offer.getDegree());
        offerDTO.setGender(offer.getGender());
        offerDTO.setSkills(offer.getSkills().split("/"));
        offerDTO.setType(offer.getType());
        offerDTO.setMinimumExperience(offer.getMinimumExperience());
        offerDTO.setRecommendedExperience(offer.getRecommendedExperience());
        offerDTO.setCompanyId(offer.getCompany().getId());

        return offerDTO;
    }
}
