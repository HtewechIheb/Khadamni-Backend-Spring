package tn.rnu.enicarthage.khadamni.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.rnu.enicarthage.khadamni.dto.requests.AddOfferDTO;
import tn.rnu.enicarthage.khadamni.dto.requests.UpdateOfferDTO;
import tn.rnu.enicarthage.khadamni.dto.responses.OfferDTO;
import tn.rnu.enicarthage.khadamni.models.Company;
import tn.rnu.enicarthage.khadamni.models.Offer;
import tn.rnu.enicarthage.khadamni.services.interfaces.CompanyService;
import tn.rnu.enicarthage.khadamni.services.interfaces.OfferService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "api/offers")
@SecurityRequirement(name = "bearerAuth")
public class OffersController {
    @Autowired
    private OfferService offerService;

    @Autowired
    private CompanyService companyService;

    @GetMapping(path = "")
    public ResponseEntity<Object> getAll() {
        List<Offer> offers = offerService.getOffers();
        List<OfferDTO> offersDTO = new ArrayList<>();

        for(Offer offer: offers){
            offersDTO.add(OfferDTO.fromModel(offer));
        }

        return ResponseEntity.ok(offersDTO);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Long id) {
        Offer offer = offerService.getOfferById(id);

        if(!Objects.nonNull(offer)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Offer With ID %d Does Not Exist!", id));
        }

        return ResponseEntity.ok(OfferDTO.fromModel(offer));
    }

    @PostMapping(path = "")
    public ResponseEntity<Object> add(@Valid @RequestBody AddOfferDTO addOfferDTO, HttpServletRequest httpRequest) {
        Company associatedCompany = companyService.getCompanyById(addOfferDTO.getCompanyId());

        if (!Objects.nonNull(associatedCompany))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Company With ID %d Does Not Exist!", addOfferDTO.getCompanyId()));
        }

        Offer offer = addOfferDTO.toModel();
        offer.setCompany(associatedCompany);

        offerService.addOffer(offer);

        String locationUrl = String.format("%s://%s:%d/api/offers/%d", httpRequest.getScheme(), httpRequest.getServerName(), httpRequest.getServerPort(), offer.getId());
        return ResponseEntity.created(URI.create(locationUrl)).body(OfferDTO.fromModel(offer));
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody UpdateOfferDTO updateOfferDTO, @PathVariable("id") Long id){
        if(!offerService.offerExists(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Offer With ID %d Does Not Exist!", id));
        }

        offerService.updateOffer(id, updateOfferDTO.toModel());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id){
        if(!offerService.offerExists(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Offer With ID %d Does Not Exist!", id));
        }

        offerService.deleteOffer(id);

        return ResponseEntity.noContent().build();
    }
}

