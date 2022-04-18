package tn.rnu.enicarthage.khadamni.services.interfaces;

import tn.rnu.enicarthage.khadamni.models.Offer;

import java.util.List;

public interface OfferService {
    List<Offer> getOffers();
    Offer getOfferById(Long id);
    boolean offerExists(Long id);
    Offer addOffer(Offer offer);
    Offer updateOffer(Long id, Offer updatedOffer);
    void deleteOffer(Long id);
}
