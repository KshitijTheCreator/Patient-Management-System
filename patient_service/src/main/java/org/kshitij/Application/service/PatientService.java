package org.kshitij.Application.service;

import org.kshitij.Application.dto.PatientRequestDTO;
import org.kshitij.Application.dto.PatientResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PatientService {
   public List<PatientResponseDTO> fetchAllPatients();
   public PatientResponseDTO createPatient(PatientRequestDTO patient);
   public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patient);
   public void deletePatient(UUID id);
}
