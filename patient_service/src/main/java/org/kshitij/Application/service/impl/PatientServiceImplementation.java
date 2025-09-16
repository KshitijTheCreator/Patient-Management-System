package org.kshitij.Application.service.impl;

import org.kshitij.Application.dto.PatientRequestDTO;
import org.kshitij.Application.dto.PatientResponseDTO;
import org.kshitij.Application.entity.Patient;
import org.kshitij.Application.exception.EmailAlreadyExistsException;
import org.kshitij.Application.exception.PatientNotExistsException;
import org.kshitij.Application.grpc.BillingServiceGrpcClient;
import org.kshitij.Application.kafka.KafkaProducer;
import org.kshitij.Application.mapper.PatientMapper;
import org.kshitij.Application.repository.PatientRepository;
import org.kshitij.Application.service.PatientService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceImplementation implements PatientService {

    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    public PatientServiceImplementation(PatientRepository patientRepository,
                                        BillingServiceGrpcClient billingServiceGrpcClient,
                                        KafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public List<PatientResponseDTO> fetchAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        //use method reference when calling the static method instead of lambda in map
        return patients.stream().map(PatientMapper::toDTO).toList();
    }

    @Override
    public PatientResponseDTO createPatient(PatientRequestDTO patient) {
        if(patientRepository.existsByEmail(patient.getEmail())) {
            System.out.println("INSIDE !!");
            throw new EmailAlreadyExistsException("A patient with this email " + "already exists"
                    + patient.getEmail());
        }
        Patient createdPatient = PatientMapper.toPatient(patient);
        patientRepository.save(createdPatient);
        String id = createdPatient.getId().toString();
        billingServiceGrpcClient.createBillingAccount(id,
                createdPatient.getName(),
                createdPatient.getEmail());
        kafkaProducer.sendEvent(createdPatient);
        return PatientMapper.toDTO(createdPatient);
    }

    @Override
    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patient) {
        Patient p = patientRepository.findById(id)
                .orElseThrow(()-> new PatientNotExistsException("Patient not found with current id"));
        if(patientRepository.existsByEmailAndIdNot(patient.getEmail(), id)){
            throw new EmailAlreadyExistsException("Email already exists");
        }
        p.setEmail(patient.getEmail());
        p.setName(patient.getName());
        p.setDateOfBirth(LocalDate.parse(patient.getDateOfBirth()));
        p.setRegisteredDate(LocalDate.parse(patient.getRegisteredDate()));

        Patient updatedPatient = patientRepository.save(p);
        return PatientMapper.toDTO(updatedPatient);
    }
    @Override
    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }






    //helper methods
//    private boolean duplicateEmailEntryChecker(String email){
//        return !patientRepository.existsPatientByEmail(email);
//    }
}

