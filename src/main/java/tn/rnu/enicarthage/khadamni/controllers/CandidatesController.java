package tn.rnu.enicarthage.khadamni.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.rnu.enicarthage.khadamni.dto.requests.AddCandidateDTO;
import tn.rnu.enicarthage.khadamni.dto.requests.UpdateCandidateDTO;
import tn.rnu.enicarthage.khadamni.dto.responses.CandidateDTO;
import tn.rnu.enicarthage.khadamni.models.Candidate;
import tn.rnu.enicarthage.khadamni.services.interfaces.CandidateService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(path = "api/candidates")
@SecurityRequirement(name = "bearerAuth")
public class CandidatesController {
    @Autowired
    private CandidateService candidateService;

    @GetMapping(path = "")
    public ResponseEntity<Object> getAll(HttpServletRequest httpRequest) {
        log.info(String.format("Get all route was accessed from [%s].", httpRequest.getRemoteAddr()));

        List<Candidate> candidates = candidateService.getCandidates();
        List<CandidateDTO> candidatesDTO = new ArrayList<>();

        for(Candidate candidate: candidates){
            candidatesDTO.add(CandidateDTO.fromModel(candidate));
        }

        log.info(String.format("Successful candidates retrieval attempt from [%s].", httpRequest.getRemoteAddr()));
        return ResponseEntity.ok(candidatesDTO);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Long id, HttpServletRequest httpRequest) {
        log.info(String.format("Get route was accessed from [%s] with id [%d].", httpRequest.getRemoteAddr(), id));
        Candidate candidate = candidateService.getCandidateById(id);

        if(!Objects.nonNull(candidate)){
            log.info(String.format("Failed candidate retrieval attempt from [%s] with id [%d]: Candidate does not exist.", httpRequest.getRemoteAddr(), id));

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Candidate With ID %d Does Not Exist!", id));
        }

        log.info(String.format("Successful candidate retrieval attempt from [%s] with id [%d].", httpRequest.getRemoteAddr(), id));
        return ResponseEntity.ok(CandidateDTO.fromModel(candidate));
    }

    @PostMapping(path = "", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Object> add(@Valid @ModelAttribute AddCandidateDTO addCandidateDTO, HttpServletRequest httpRequest) {
        log.info(String.format("Add route was accessed from [%s].", httpRequest.getRemoteAddr()));
        try {
            Candidate addedCandidate = candidateService.addCandidate(addCandidateDTO.toModel());

            String locationUrl = String.format("%s://%s:%d/api/candidates/%d", httpRequest.getScheme(), httpRequest.getServerName(), httpRequest.getServerPort(), addedCandidate.getId());

            log.info(String.format("Successful candidate creation attempt from [%s].", httpRequest.getRemoteAddr()));
            return ResponseEntity.created(URI.create(locationUrl)).body(CandidateDTO.fromModel(addedCandidate));
        }
        catch (Exception exception) {
            log.error(String.format("Internal error occurred during candidate creation attempt from [%s].", httpRequest.getRemoteAddr()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Error Occurred While Adding Candidate!");
        }
    }

    @GetMapping(path = "{id}/{resource}")
    public ResponseEntity<Object> downloadResource(@PathVariable("id") Long id, @PathVariable("resource") String resource, HttpServletRequest httpRequest){
        log.info(String.format("Download resource route was accessed from [%s] with resource [%s] and id [%d].", httpRequest.getRemoteAddr(), resource, id));

        Candidate candidate = candidateService.getCandidateById(id);

        if(!Objects.nonNull(candidate)){
            log.info(String.format("Failed resource [%s] retrieval attempt from [%s] with id [%d]: Candidate does not exist.", resource, httpRequest.getRemoteAddr(), id));

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Candidate With ID %d Does Not Exist!", id));
        }

        switch (resource) {
            case "photo":
                log.info(String.format("Successful resource [%s] retrieval attempt from [%s] with id [%d].", resource, httpRequest.getRemoteAddr(), id));

                return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", candidate.getPhotoFileName()))
                        .body(candidate.getPhotoFile());
            case "resume":
                log.info(String.format("Successful resource [%s] retrieval attempt from [%s] with id [%d].", resource, httpRequest.getRemoteAddr(), id));

                return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", candidate.getResumeFileName()))
                        .body(candidate.getResumeFile());
            default:
                log.info(String.format("Failed resource [%s] retrieval attempt from [%s] with id [%d]: Resource does not exist.", resource, httpRequest.getRemoteAddr(), id));

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Candidate Resource %s Does Not Exist!", resource));
        }
    }

    @PutMapping(path = "{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Object> update(@Valid @ModelAttribute UpdateCandidateDTO updateCandidateDTO, @PathVariable("id") Long id, HttpServletRequest httpRequest){
        log.info(String.format("Update route was accessed from [%s].", httpRequest.getRemoteAddr()));
        try {
            if(!candidateService.candidateExists(id)){
                log.info(String.format("Failed candidate update attempt from [%s] with id [%d]: Candidate does not exist.", httpRequest.getRemoteAddr(), id));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Candidate With ID %d Does Not Exist!", id));
            }

            candidateService.updateCandidate(id, updateCandidateDTO.toModel());

            log.info(String.format("Successful candidate update attempt from [%s] with id [%d].", httpRequest.getRemoteAddr(), id));
            return ResponseEntity.noContent().build();
        }
        catch (IOException exception) {
            log.error(String.format("Internal error occurred during candidate update attempt from [%s].", httpRequest.getRemoteAddr()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Error Occurred While Updating Candidate!");
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id, HttpServletRequest httpRequest){
        log.info(String.format("Delete route was accessed from [%s].", httpRequest.getRemoteAddr()));
        if(!candidateService.candidateExists(id)){
            log.info(String.format("Failed candidate deletion attempt from [%s] with id [%d]: Candidate does not exist.", httpRequest.getRemoteAddr(), id));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Candidate With ID %d Does Not Exist!", id));
        }

        candidateService.deleteCandidate(id);

        log.info(String.format("Successful candidate deletion attempt from [%s] with id [%d].", httpRequest.getRemoteAddr(), id));
        return ResponseEntity.noContent().build();
    }
}
