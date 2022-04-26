package tn.rnu.enicarthage.khadamni.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.internal.util.StringHelper;
import tn.rnu.enicarthage.khadamni.models.Offer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Objects;

import static tn.rnu.enicarthage.khadamni.shared.Globals.parseNullableString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOfferDTO {
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

    public Offer toModel() {
        Offer offer = new Offer();
        offer.setIndustry(parseNullableString(industry));
        offer.setTitle(parseNullableString(title));
        offer.setDescription(parseNullableString(description));
        offer.setSpots(spots);
        offer.setSalary(salary);
        offer.setDegree(parseNullableString(degree));
        offer.setGender(parseNullableString(gender));
        offer.setSkills(Objects.nonNull(skills) ? String.join("/", skills) : null);
        offer.setType(parseNullableString(type));
        offer.setMinimumExperience(parseNullableString(minimumExperience));
        offer.setRecommendedExperience(parseNullableString(recommendedExperience));

        return offer;
    }
}
