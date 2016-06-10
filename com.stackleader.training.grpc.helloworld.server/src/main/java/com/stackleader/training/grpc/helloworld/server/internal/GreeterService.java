package com.stackleader.training.grpc.helloworld.server.internal;

import com.stackleader.training.grpc.helloworld.api.GreeterGrpc;
import com.stackleader.training.grpc.helloworld.api.GreeterGrpc.AbstractGreeter;
import com.stackleader.training.grpc.helloworld.api.HelloReply;
import com.stackleader.training.grpc.helloworld.api.HelloRequest;
import io.grpc.stub.StreamObserver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, service = AbstractGreeter.class)
public class GreeterService extends GreeterGrpc.AbstractGreeter {

    private static final Logger LOG = LoggerFactory.getLogger(GreeterService.class);

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        LOG.info("sayHello endpoint received request from " + request.getName());
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
