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
public class UpdateOfferDTO {
    private String category;
    private String title;
    private String description;
    private Integer spots;
    private String type;
    private String experienceLowerBound;
    private String experienceUpperBound;

    public Offer toModel() {
        Offer offer = new Offer();
        offer.setCategory(parseNullableString(category));
        offer.setTitle(parseNullableString(title));
        offer.setDescription(parseNullableString(description));
        offer.setSpots(spots);
        offer.setType(parseNullableString(type));
        offer.setExperienceLowerBound(parseNullableString(experienceLowerBound));
        offer.setExperienceUpperBound(parseNullableString(experienceUpperBound));

        return offer;
    }
}
