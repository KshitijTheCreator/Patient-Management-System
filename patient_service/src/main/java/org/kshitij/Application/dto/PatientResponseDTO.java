package org.kshitij.Application.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class PatientResponseDTO {
    private String id;
    private String name;
    private String email;
    private String address;
    private String birthDate;
}
