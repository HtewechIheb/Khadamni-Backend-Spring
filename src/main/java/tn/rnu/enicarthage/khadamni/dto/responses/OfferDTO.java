package tn.rnu.enicarthage.khadamni.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.rnu.enicarthage.khadamni.models.Offer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferDTO {
    private Long id;
    private String category;
    private String title;
    private String description;
    private Integer spots;
    private String type;
    private String experienceLowerBound;
    private String experienceUpperBound;

    public static OfferDTO fromModel(Offer offer) {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(offer.getId());
        offerDTO.setCategory(offer.getCategory());
        offerDTO.setTitle(offer.getTitle());
        offerDTO.setDescription(offer.getDescription());
        offerDTO.setSpots(offer.getSpots());
        offerDTO.setType(offer.getType());
        offerDTO.setExperienceLowerBound(offer.getExperienceLowerBound());
        offerDTO.setExperienceUpperBound(offer.getExperienceUpperBound());

        return offerDTO;
    }
}
