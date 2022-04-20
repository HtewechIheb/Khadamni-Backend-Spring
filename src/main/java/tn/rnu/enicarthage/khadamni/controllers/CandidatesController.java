package tn.rnu.enicarthage.khadamni.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

@RestController
@RequestMapping(path = "api/candidates")
@SecurityRequirement(name = "bearerAuth")
public class CandidatesController {
    @Autowired
    private CandidateService candidateService;

    @GetMapping(path = "")
    public ResponseEntity<Object> getAll() {
        List<Candidate> candidates = candidateService.getCandidates();
        List<CandidateDTO> candidatesDTO = new ArrayList<>();

        for(Candidate candidate: candidates){
            candidatesDTO.add(CandidateDTO.fromModel(candidate));
        }

        return ResponseEntity.ok(candidatesDTO);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Long id) {
        Candidate candidate = candidateService.getCandidateById(id);

        if(!Objects.nonNull(candidate)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Candidate With ID %d Does Not Exist!", id));
        }

        return ResponseEntity.ok(CandidateDTO.fromModel(candidate));
    }

    @PostMapping(path = "", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Object> add(@Valid @ModelAttribute AddCandidateDTO addCandidateDTO, HttpServletRequest httpRequest) {
        try {
            Candidate addedCandidate = candidateService.addCandidate(addCandidateDTO.toModel());

            String locationUrl = String.format("%s://%s:%d/api/candidates/%d", httpRequest.getScheme(), httpRequest.getServerName(), httpRequest.getServerPort(), addedCandidate.getId());

            return ResponseEntity.created(URI.create(locationUrl)).body(CandidateDTO.fromModel(addedCandidate));
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Error Occurred While Adding Candidate!");
        }
    }

    @GetMapping(path = "{id}/{resource}")
    public ResponseEntity<Object> downloadPhoto(@PathVariable("id") Long id, @PathVariable("resource") String resource){
        Candidate candidate = candidateService.getCandidateById(id);

        if(!Objects.nonNull(candidate)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Candidate With ID %d Does Not Exist!", id));
        }

        switch (resource) {
            case "photo":
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", candidate.getPhotoFileName()))
                        .body(candidate.getPhotoFile());
            case "resume":
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", candidate.getResumeFileName()))
                        .body(candidate.getResumeFile());
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Candidate Resource %s Does Not Exist!", resource));
        }
    }

    @PutMapping(path = "{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Object> update(@Valid @ModelAttribute UpdateCandidateDTO updateCandidateDTO, @PathVariable("id") Long id){
        try {
            if(!candidateService.candidateExists(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Candidate With ID %d Does Not Exist!", id));
            }

            candidateService.updateCandidate(id, updateCandidateDTO.toModel());

            return ResponseEntity.noContent().build();
        }
        catch (IOException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Error Occurred While Updating Candidate!");
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id){
        if(!candidateService.candidateExists(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Candidate With ID %d Does Not Exist!", id));
        }

        candidateService.deleteCandidate(id);

        return ResponseEntity.noContent().build();
    }
}
