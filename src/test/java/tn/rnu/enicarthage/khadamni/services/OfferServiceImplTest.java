package tn.rnu.enicarthage.khadamni.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.rnu.enicarthage.khadamni.models.Offer;
import tn.rnu.enicarthage.khadamni.repositories.OfferRepository;
import tn.rnu.enicarthage.khadamni.services.interfaces.OfferService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OfferServiceImplTest {
    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private OfferService offerService = new OfferServiceImpl();

    @Test
    @DisplayName("getOffers(): Should return all offers.")
    void whenGetOffers_thenReturnAllOffers() {
        ArrayList<Offer> offers = new ArrayList<>();
        offers.add(new Offer(1L, "Industry", "Title", "Description", null, null, "Degree", "Gender", "Skills", "Type", "Minimum Experience", "Recommended Experience", null, null));
        offers.add(new Offer(2L, "Industry", "Title", "Description", null, null, "Degree", "Gender", "Skills", "Type", "Minimum Experience", "Recommended Experience", null, null));

        Mockito.when(offerRepository.findAll()).thenReturn(offers);

        List<Offer> offersToTest = offerService.getOffers();

        assertIterableEquals(offersToTest, offers);
    }

    @Test
    @DisplayName("getOfferById(): Should return the correct offer when a valid id is provided.")
    void whenGetOfferById_withValidId_thenReturnOffer() {
        Offer offer = new Offer(1L, "Industry", "Title", "Description", null, null, "Degree", "Gender", "Skills", "Type", "Minimum Experience", "Recommended Experience", null, null);

        Mockito.when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));

        Offer offerToTest = offerService.getOfferById(1L);

        assertEquals(offerToTest, offer);
    }

    @Test
    @DisplayName("getOfferById(): Should return null when an invalid id is provided.")
    void whenGetOfferById_withInvalidId_thenReturnNull() {
        Mockito.when(offerRepository.findById(1L)).thenReturn(Optional.empty());

        Offer offerToTest = offerService.getOfferById(1L);

        assertNull(offerToTest);
    }

    @Test
    @DisplayName("offerExists(): Should return true when a valid offer's id is provided.")
    void whenOfferExists_withValidOfferId_thenReturnTrue() {
        Mockito.when(offerRepository.existsById(1L)).thenReturn(true);

        boolean result = offerService.offerExists(1L);

        assertTrue(result);
    }

    @Test
    @DisplayName("offerExists(): Should return false when an invalid offer's id is provided.")
    void whenOfferExists_withInvalidOfferId_thenReturnFalse() {
        Mockito.when(offerRepository.existsById(1L)).thenReturn(false);

        boolean result = offerService.offerExists(1L);

        assertFalse(result);
    }

    @Test
    @DisplayName("addOffer(): Should return added offer.")
    void whenAddOffer_thenReturnAddedOffer() {
        Offer offer = new Offer(1L, "Industry", "Title", "Description", null, null, "Degree", "Gender", "Skills", "Type", "Minimum Experience", "Recommended Experience", null, null);

        Mockito.when(offerRepository.save(offer)).thenReturn(offer);

        Offer offerToTest = offerService.addOffer(offer);

        Mockito.verify(offerRepository, Mockito.times(1)).save(offer);
        assertEquals(offerToTest, offer);
    }

    @Test
    @DisplayName("addOffer(): Should return updated offer when a valid id is provided.")
    void whenUpdateOffer_withValidId_thenReturnUpdatedOffer() {
        Offer offer = new Offer(1L, "Industry", "Title", "Description", null, null, "Degree", "Gender", "Skills", "Type", "Minimum Experience", "Recommended Experience", null, null);
        Offer updatedOffer = new Offer(1L, "Updated Industry", "Updated Title", "Updated Description", null, null, "Updated Degree", "Updated Gender", "Updated Skills", "Updated Type", "Updated Minimum Experience", "Updated Recommended Experience", null, null);

        Mockito.when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));

        Offer offerToTest = offerService.updateOffer(1L, updatedOffer);

        Mockito.verify(offerRepository, Mockito.times(1)).save(offer);
        assertEquals(offerToTest, updatedOffer);
    }

    @Test
    @DisplayName("addOffer(): Should return null when an invalid id is provided.")
    void whenUpdateOffer_withInvalidId_thenReturnNull() {
        Mockito.when(offerRepository.findById(1L)).thenReturn(Optional.empty());

        Offer offerToTest = offerService.updateOffer(1L, new Offer());

        assertNull(offerToTest);
    }

    @Test
    void deleteOffer() {
        offerService.deleteOffer(1L);

        Mockito.verify(offerRepository, Mockito.times(1)).deleteById(1L);
    }
}