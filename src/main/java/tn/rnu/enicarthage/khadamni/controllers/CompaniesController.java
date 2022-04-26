package tn.rnu.enicarthage.khadamni.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.rnu.enicarthage.khadamni.dto.requests.AddCompanyDTO;
import tn.rnu.enicarthage.khadamni.dto.requests.UpdateCompanyDTO;
import tn.rnu.enicarthage.khadamni.dto.responses.CompanyDTO;
import tn.rnu.enicarthage.khadamni.models.Company;
import tn.rnu.enicarthage.khadamni.services.interfaces.CompanyService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(path = "api/companies")
@SecurityRequirement(name = "bearerAuth")
public class CompaniesController {
    @Autowired
    private CompanyService companyService;

    @GetMapping(path = "")
    public ResponseEntity<Object> getAll(HttpServletRequest httpRequest) {
        log.info(String.format("Get all route was accessed from [%s].", httpRequest.getRemoteAddr()));

        List<Company> companies = companyService.getCompanies();
        List<CompanyDTO> companiesDTO = new ArrayList<>();

        for(Company company: companies){
            companiesDTO.add(CompanyDTO.fromModel(company));
        }

        log.info(String.format("Successful companies retrieval attempt from [%s].", httpRequest.getRemoteAddr()));
        return ResponseEntity.ok(companiesDTO);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Long id, HttpServletRequest httpRequest) {
        log.info(String.format("Get route was accessed from [%s] with id [%d].", httpRequest.getRemoteAddr(), id));
        Company company = companyService.getCompanyById(id);

        if(!Objects.nonNull(company)){
            log.info(String.format("Failed company retrieval attempt from [%s] with id [%d]: Company does not exist.", httpRequest.getRemoteAddr(), id));

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Company With ID %d Does Not Exist!", id));
        }

        log.info(String.format("Successful company retrieval attempt from [%s] with id [%d].", httpRequest.getRemoteAddr(), id));
        return ResponseEntity.ok(CompanyDTO.fromModel(company));
    }

    @PostMapping(path = "", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Object> add(@Valid @ModelAttribute AddCompanyDTO addCompanyDTO, HttpServletRequest httpRequest) {
        log.info(String.format("Add route was accessed from [%s].", httpRequest.getRemoteAddr()));
        try {
            Company addedCompany = companyService.addCompany(addCompanyDTO.toModel());

            String locationUrl = String.format("%s://%s:%d/api/companies/%d", httpRequest.getScheme(), httpRequest.getServerName(), httpRequest.getServerPort(), addedCompany.getId());

            log.info(String.format("Successful company creation attempt from [%s].", httpRequest.getRemoteAddr()));
            return ResponseEntity.created(URI.create(locationUrl)).body(CompanyDTO.fromModel(addedCompany));
        }
        catch (IOException exception) {
            log.error(String.format("Internal error occurred during company creation attempt from [%s].", httpRequest.getRemoteAddr()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Error Occurred While Adding Company!");
        }
    }

    @GetMapping(path = "{id}/photo")
    public ResponseEntity<Object> downloadPhoto(@PathVariable("id") Long id, HttpServletRequest httpRequest){
        log.info(String.format("Download photo route was accessed from [%s] and id [%d].", httpRequest.getRemoteAddr(), id));

        Company company = companyService.getCompanyById(id);

        if(!Objects.nonNull(company)){
            log.info(String.format("Failed photo retrieval attempt from [%s] with id [%d]: Company does not exist.", httpRequest.getRemoteAddr(), id));

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Company With ID %d Does Not Exist!", id));
        }

        log.info(String.format("Successful photo retrieval attempt from [%s] with id [%d].", httpRequest.getRemoteAddr(), id));
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", company.getLogoFileName()))
                .body(company.getLogoFile());
    }

    @PutMapping(path = "{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Object> update(@Valid @ModelAttribute UpdateCompanyDTO updateCompanyDTO, @PathVariable("id") Long id, HttpServletRequest httpRequest){
    log.info(String.format("Update route was accessed from [%s].", httpRequest.getRemoteAddr()));
        try {
            if(!companyService.companyExists(id)){
                log.info(String.format("Failed company update attempt from [%s] with id [%d]: Company does not exist.", httpRequest.getRemoteAddr(), id));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Company With ID %d Does Not Exist!", id));
            }

            companyService.updateCompany(id, updateCompanyDTO.toModel());

            log.info(String.format("Successful company update attempt from [%s] with id [%d].", httpRequest.getRemoteAddr(), id));
            return ResponseEntity.noContent().build();
        }
        catch (IOException exception) {
            log.error(String.format("Internal error occurred during company update attempt from [%s].", httpRequest.getRemoteAddr()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Error Occurred While Updating Company!");
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id, HttpServletRequest httpRequest){
        log.info(String.format("Delete route was accessed from [%s].", httpRequest.getRemoteAddr()));
        if(!companyService.companyExists(id)){
            log.info(String.format("Failed company deletion attempt from [%s] with id [%d]: Company does not exist.", httpRequest.getRemoteAddr(), id));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Company With ID %d Does Not Exist!", id));
        }

        companyService.deleteCompany(id);

        log.info(String.format("Successful company deletion attempt from [%s] with id [%d].", httpRequest.getRemoteAddr(), id));
        return ResponseEntity.noContent().build();
    }
}
