package org.kshitij.Application.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BillingServiceGrpcClient {
//    blocking stub below is the blocking nested class in grpc generated class
//    which is synchronous (blocking) in nature which waits for the response 
//    from the billing service microservice
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;
    String stDebug = "BillingServiceGrpcClient";
    
    public BillingServiceGrpcClient(
            @Value("${billing.service.address:localhost}") String serviceAddress,
            @Value("${billing.service.grpc.port:9001}") int serverPort
    ){

        log.info("{} :: BillingServiceGrpcClient :: Creating stub", stDebug);
        log.info("Connecting to service address: {} and grpc port number: {}", serviceAddress, serverPort);


//        Managed channels are like TCP but stronger which is abstraction of :
//        (a.) network connection
//        (b.) connection pooling : reuse same connection for multiple calls
        ManagedChannel channel =  ManagedChannelBuilder.forAddress(serviceAddress, serverPort)
                .usePlaintext().build();
        blockingStub = BillingServiceGrpc.newBlockingStub(channel);
        log.info("{} :: BillingServiceGrpcClient :: Stub created successfully", stDebug);
    }

    public BillingResponse createBillingAccount(String patientId, String email, String name) {
        log.info("{} :: createBillingAccount :: Entry", stDebug);
        BillingRequest req = BillingRequest.newBuilder()
                .setEmail(email)
                .setName(name)
                .setPatientId(patientId)
                .build();

        BillingResponse response = blockingStub.createBillingAccount(req);
        log.info("{} :: createBillingAccount :: Exit", stDebug);
        return response;
    }
}
