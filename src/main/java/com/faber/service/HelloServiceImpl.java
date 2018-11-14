package com.faber.service;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.faber.service.HelloServiceGrpc.HelloServiceImplBase;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.atomic.AtomicBoolean;
//</editor-fold>

/**
 *
 * @author Nguyen Duc Thien
 * @email nguyenducthien@fabercompany.co.jp
 */
public class HelloServiceImpl extends HelloServiceImplBase {

    @Override
    public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        System.out.println("Get request " + request.getFirstName() + " " + request.getLastName());
        String greeting = new StringBuilder().append("Hello, ")
                .append(request.getFirstName())
                .append(" ")
                .append(request.getLastName())
                .toString();

        //Prepare response
        HelloResponse response = HelloResponse.newBuilder()
                .setGreeting(greeting)
                .build();

        //Response to client
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void helloStreaming(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(500);// prevent streaming too fast, just for tutorial
                String greeting = new StringBuilder().append("Hello, ")
                        .append(request.getFirstName())
                        .append(" ")
                        .append(i)
                        .append(" ")
                        .append(request.getLastName())
                        .toString();
                System.out.println("Streaming response to client: " + greeting);

                //Prepare response
                HelloResponse response = HelloResponse.newBuilder()
                        .setGreeting(greeting)
                        .build();

                //Streaming to client
                responseObserver.onNext(response);
            } catch (InterruptedException ex) {
            }

        }

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<HelloRequest> helloStreamingBidirectional(StreamObserver<HelloResponse> responseObserver) {

        final ServerCallStreamObserver<HelloResponse> serverCallStreamObserver = (ServerCallStreamObserver<HelloResponse>) responseObserver;
        serverCallStreamObserver.disableAutoInboundFlowControl();

        // Guard against spurious onReady() calls caused by a race between onNext() and onReady(). If the transport
        // toggles isReady() from false to true while onNext() is executing, but before onNext() checks isReady(),
        // request(1) would be called twice - once by onNext() and once by the onReady() scheduled during onNext()'s
        // execution.
        final AtomicBoolean wasReady = new AtomicBoolean(false);

        // Set up a back-pressure-aware consumer for the request stream. The onReadyHandler will be invoked
        // when the consuming side has enough buffer space to receive more messages.
        //
        // Note: the onReadyHandler's invocation is serialized on the same thread pool as the incoming StreamObserver's
        // onNext(), onError(), and onComplete() handlers. Blocking the onReadyHandler will prevent additional messages
        // from being processed by the incoming StreamObserver. The onReadyHandler must return in a timely manor or else
        // message processing throughput will suffer.
        serverCallStreamObserver.setOnReadyHandler(() -> {
            if (serverCallStreamObserver.isReady() && wasReady.compareAndSet(false, true)) {
                // Signal the request sender to send one message. This happens when isReady() turns true, signaling that
                // the receive buffer has enough free space to receive more messages. Calling request() serves to prime
                // the message pump.
                serverCallStreamObserver.request(1);
            }
        });

        // Give gRPC a StreamObserver that can observe and process incoming requests.
        return new StreamObserver<HelloRequest>() {
            @Override
            public void onNext(HelloRequest request) {
                // Process the request and send a response or an error.
                // Accept and enqueue the request.
                String name = request.getFirstName();

                try {
                    // Simulate server "work"
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }

                // Send a response.
                String message = "Hello " + name;
                HelloResponse reply = HelloResponse.newBuilder().setGreeting(message).build();
                responseObserver.onNext(reply);

                // Check the provided ServerCallStreamObserver to see if it is still ready to accept more messages.
                if (serverCallStreamObserver.isReady()) {
                    serverCallStreamObserver.request(1);
                } else {
                    // If not, note that back-pressure has begun.
                    wasReady.set(false);
                }

            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
