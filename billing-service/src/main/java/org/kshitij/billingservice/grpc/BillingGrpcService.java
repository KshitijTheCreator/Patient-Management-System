package org.kshitij.billingservice.grpc;
import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;


import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//grpc allows us to pass multiple requests unlike rest call where only one response is sent
//hence we call onNext method here to handle the case of multiple response
@GrpcService
public class BillingGrpcService extends BillingServiceImplBase  {
    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(BillingRequest request, StreamObserver<BillingResponse> responseObserver) {
        log.info("BillingGrpcService :: Entry");
        BillingResponse response = BillingResponse.newBuilder().
                setAccountId("123").
                setStatus("Active").
                build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}














