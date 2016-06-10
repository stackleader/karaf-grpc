package com.stackleader.training.grpc.helloworld.client;

import com.stackleader.training.grpc.helloworld.api.GreeterGrpc;
import com.stackleader.training.grpc.helloworld.api.HelloReply;
import com.stackleader.training.grpc.helloworld.api.HelloRequest;
import com.stackleader.training.grpc.helloworld.server.GrpcServer;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.okhttp.OkHttpChannelBuilder;
import java.util.concurrent.TimeUnit;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true)
public class HelloWorldClient {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldClient.class);

    private final String host = "localhost";
    private final int port = 5000;
    private ManagedChannel channel;
    private GreeterGrpc.GreeterBlockingStub blockingStub;

    @Activate
    public void activate() {
        channel = OkHttpChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .build();
        blockingStub = GreeterGrpc.newBlockingStub(channel);
        try {
            greet("world");
            shutdown();
        } catch (InterruptedException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * Say hello to server.
     */
    public void greet(String name) {
        LOG.info("Will try to greet " + name + " ...");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;
        try {
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException e) {
            LOG.warn("RPC failed: {0}", e.getStatus());
            return;
        }
        LOG.info("Greeting: " + response.getMessage());
    }

    @Reference
    public void setGrpcServer(GrpcServer grpcServer) {
        //ensures the server has started before we attempt to connect
    }

}
