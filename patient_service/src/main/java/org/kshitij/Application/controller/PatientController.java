package org.kshitij.Application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import org.kshitij.Application.dto.PatientRequestDTO;
import org.kshitij.Application.dto.PatientResponseDTO;
import org.kshitij.Application.dto.validators.CreatePatientValidationGroup;
import org.kshitij.Application.exception.EmailAlreadyExistsException;
import org.kshitij.Application.repository.PatientRepository;
import org.kshitij.Application.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@Tag(name = "Endpoints for managing the patients")
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;
    private final String stDebug= "PatientController";
    public PatientController(PatientService patientService, PatientRepository patientRepository) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(description = "Fetches all the patients in the database",
            summary = "Gets all description of the patients in the db")
    public ResponseEntity<List<PatientResponseDTO>> findAllPatients() {
        List<PatientResponseDTO> patients =patientService.fetchAllPatients();
        return ResponseEntity.ok().body(patients);
    }

    @PostMapping
    @Operation(description = "Used to create new patient entity",
        summary = "Creates new entity to database with unique validation of the email of patients"
    )
    public ResponseEntity<PatientResponseDTO> createPatient(
            @Validated({Default.class, CreatePatientValidationGroup.class})
            @RequestBody PatientRequestDTO patientRequestDTO)  {
        log.info("{} :: createPatient :: Entry",stDebug);
        PatientResponseDTO patientResponseDTO =patientService.createPatient(patientRequestDTO);

        return ResponseEntity.ok().body(patientResponseDTO);
    }
    @PutMapping("/{id}")
    @Operation(description = "Used to update an existing entity",
            summary = "Updates the already present patient entity in the database")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id,
            @RequestBody @Validated({Default.class}) PatientRequestDTO patientRequestDTO)  {
        PatientResponseDTO response = patientService.updatePatient(id, patientRequestDTO);
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/{id}")
    @Operation(description = "Used to delete a patient entity",
            summary = "Deletes the already present patient entity in the database"
    )
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
