package org.kshitij.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics ="patient", groupId = "analytics-service")
    public void consumerEvent(byte[] event) {
        try{
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            //consuming the details below .
            String id = patientEvent.getPatientId();
            String name = patientEvent.getName();
            String email = patientEvent.getEmail();
            log.info("""
                    Analytics service ran successfully, \
                    /n Patient's Id: {}\
                    
                     Patient's Name: {}\
                    \s
                    Patient's Email: {}""", id, name, email);
        } catch(InvalidProtocolBufferException e) {
            log.error("InvalidProtocolBufferException: {}", e.getMessage());
        }
    }
}
