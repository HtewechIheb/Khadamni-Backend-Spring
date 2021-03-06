package tn.rnu.enicarthage.khadamni.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.rnu.enicarthage.khadamni.models.Offer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Objects;

import static tn.rnu.enicarthage.khadamni.shared.Globals.parseNullableString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOfferDTO {
    @NotBlank
    private String industry;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private Integer spots;

    private Double salary;

    private String degree;

    private String gender;

    private String[] skills;

    @NotBlank
    private String type;

    @NotBlank
    private String minimumExperience;

    private String recommendedExperience;

    public Offer toModel() {
        Offer offer = new Offer();
        offer.setIndustry(industry);
        offer.setTitle(title);
        offer.setDescription(description);
        offer.setSpots(spots);
        offer.setSalary(salary);
        offer.setDegree(parseNullableString(degree));
        offer.setGender(parseNullableString(gender));
        offer.setSkills(Objects.nonNull(skills) ? String.join("/", skills) : null);
        offer.setType(type);
        offer.setMinimumExperience(minimumExperience);
        offer.setRecommendedExperience(parseNullableString(recommendedExperience));

        return offer;
    }
}
