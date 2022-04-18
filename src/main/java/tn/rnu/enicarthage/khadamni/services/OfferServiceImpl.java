package tn.rnu.enicarthage.khadamni.services;

import org.hibernate.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.rnu.enicarthage.khadamni.models.Offer;
import tn.rnu.enicarthage.khadamni.repositories.OfferRepository;
import tn.rnu.enicarthage.khadamni.services.interfaces.OfferService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class OfferServiceImpl implements OfferService {
    @Autowired
    private OfferRepository offerRepository;

    @Override
    public List<Offer> getOffers() {
        return offerRepository.findAll();
    }

    @Override
    public Offer getOfferById(Long id) {
        return offerRepository.findById(id).orElse(null);
    }

    @Override
    public boolean offerExists(Long id) {
        return offerRepository.existsById(id);
    }

    @Override
    public Offer addOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    @Override
    public Offer updateOffer(Long id, Offer updatedOffer) {
        Offer offerToUpdate = offerRepository.findById(id).orElse(null);

        if(Objects.nonNull(offerToUpdate)){
            if(!StringHelper.isBlank(updatedOffer.getCategory())){
                offerToUpdate.setCategory(updatedOffer.getCategory());
            }
            if(!StringHelper.isBlank(updatedOffer.getTitle())){
                offerToUpdate.setTitle(updatedOffer.getTitle());
            }
            if(!StringHelper.isBlank(updatedOffer.getDescription())){
                offerToUpdate.setDescription(updatedOffer.getDescription());
            }
            if(Objects.nonNull(updatedOffer.getSpots())){
                offerToUpdate.setSpots(updatedOffer.getSpots());
            }
            if(!StringHelper.isBlank(updatedOffer.getType())){
                offerToUpdate.setType(updatedOffer.getType());
            }
            if(!StringHelper.isBlank(updatedOffer.getExperienceLowerBound())){
                offerToUpdate.setExperienceLowerBound(updatedOffer.getExperienceLowerBound());
            }
            if(!StringHelper.isBlank(updatedOffer.getExperienceUpperBound())){
                offerToUpdate.setExperienceUpperBound(updatedOffer.getExperienceUpperBound());
            }

            offerRepository.save(offerToUpdate);
        }

        return offerToUpdate;
    }

    @Override
    public void deleteOffer(Long id) {
        offerRepository.deleteById(id);
    }
}
