package tn.rnu.enicarthage.khadamni.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.rnu.enicarthage.khadamni.models.Offer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static tn.rnu.enicarthage.khadamni.shared.Globals.parseNullableString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOfferDTO {
    @NotBlank
    private String category;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private Integer spots;

    @NotBlank
    private String type;

    @NotBlank
    private String experienceLowerBound;

    private String experienceUpperBound;

    @NotNull
    private Long companyId;

    public Offer toModel() {
        Offer offer = new Offer();
        offer.setCategory(category);
        offer.setTitle(title);
        offer.setDescription(description);
        offer.setSpots(spots);
        offer.setType(type);
        offer.setExperienceLowerBound(experienceLowerBound);
        offer.setExperienceUpperBound(parseNullableString(experienceUpperBound));

        return offer;
    }
}
