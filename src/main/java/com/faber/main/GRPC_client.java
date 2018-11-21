package com.faber.main;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.faber.service.HelloRequest;
import com.faber.service.HelloResponse;
import com.faber.service.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
import io.grpc.stub.StreamObserver;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
//</editor-fold>

/**
 *
 * @author Nguyen Duc Thien
 */
public class GRPC_client {

    public static ManagedChannel channel = null;

    public static void main(String[] args) throws InterruptedException {
        int coreProcess = Runtime.getRuntime().availableProcessors();//Get num of core process of OS
        EventLoopGroup bossEventGroup = new NioEventLoopGroup(coreProcess * 2);// should be = core process for best performance

        //Create GRPC client to host
        channel = NettyChannelBuilder.forAddress("172.30.4.165", 9000)
                .usePlaintext()//get response as plain text
                .enableRetry()//enable retry when lost connection
                .maxRetryAttempts(5)//attempt to connect 5 times before throw error
                .maxInboundMessageSize(Integer.MAX_VALUE)//
                .eventLoopGroup(bossEventGroup)// thread for execute request
                .enableFullStreamDecompression()//enable compression stream in client and server
                .executor(new ForkJoinPool(coreProcess * 4, ForkJoinPool.defaultForkJoinWorkerThreadFactory, (Thread t, Throwable e) -> {
                    t.interrupt();
                }, true))
                .build();

        //request -> response
        blockingConnect();//Blocking connect to hello function and get response
        nonBlockingConnect();//non blocking connect to hello function and get response

        //request -> (Streaming) response
        blockingStreamingConnect();//blocking connect to hello function and get streaming response
        nonBlockingStreamingConnect();//blocking connect to hello function and get streaming response

        //!!!!!!!! in develop
        //(streaming)request -> (Streaming) response
        blockingStreamingBiConnect();//blocking connect to hello function and get streaming response

        channel.awaitTermination(60, TimeUnit.MINUTES);//shutdown hangout
    }

    public static void blockingConnect() {
        //Connect to service by blocking method
        HelloServiceGrpc.HelloServiceBlockingStub stub = HelloServiceGrpc.newBlockingStub(channel);
        HelloResponse helloResponse = stub.hello(HelloRequest.newBuilder()
                .setFirstName("Baeldung")
                .setLastName("gRPC")
                .build());
        System.out.println("get async response: " + helloResponse.getGreeting());
    }

    public static void nonBlockingConnect() {
        //Connect to service by non-blocking method
        HelloServiceGrpc.HelloServiceStub stub = HelloServiceGrpc.newStub(channel);

        stub.hello(HelloRequest.newBuilder()
                .setFirstName("Baeldung")
                .setLastName("gRPC")
                .build(), new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(HelloResponse value) {
                System.out.println("get async response:" + value.getGreeting());// response callback here
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
            }
        });
    }

    public static void blockingStreamingConnect() {
        //Connect to service by blocking method
        HelloServiceGrpc.HelloServiceBlockingStub stub = HelloServiceGrpc.newBlockingStub(channel);

        Iterator<HelloResponse> helloResponses = stub.helloStreaming(HelloRequest.newBuilder()
                .setFirstName("Baeldung")
                .setLastName("gRPC")
                .build());

        while (helloResponses.hasNext()) {
            HelloResponse helloResponse = helloResponses.next();
            System.out.println("Get streaming response blocking: " + helloResponse.getGreeting());
        }
    }

    public static void nonBlockingStreamingConnect() {
        //Connect to service by blocking method
        HelloServiceGrpc.HelloServiceStub stub = HelloServiceGrpc.newStub(channel);

        stub.helloStreaming(HelloRequest.newBuilder()
                .setFirstName("Baeldung")
                .setLastName("gRPC")
                .build(), new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(HelloResponse value) {
                System.out.println("Get streaming response non-blocking: " + value.getGreeting());
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
            }
        });
    }

    public static void blockingStreamingBiConnect() {
        //Connect to service by blocking method
        HelloServiceGrpc.HelloServiceStub stub = HelloServiceGrpc.newStub(channel);
        // When using manual flow-control and back-pressure on the client, the ClientResponseObserver handles both
        // request and response streams.
        ClientResponseObserver<HelloRequest, HelloResponse> clientResponseObserver
                = new ClientResponseObserver<HelloRequest, HelloResponse>() {

            ClientCallStreamObserver<HelloRequest> requestStream;

            @Override
            public void beforeStart(final ClientCallStreamObserver<HelloRequest> requestStream) {
                this.requestStream = requestStream;
                // Set up manual flow control for the response stream. It feels backwards to configure the response
                // stream's flow control using the request stream's observer, but this is the way it is.
                requestStream.disableAutoInboundFlowControl();

                // Set up a back-pressure-aware producer for the request stream. The onReadyHandler will be invoked
                // when the consuming side has enough buffer space to receive more messages.
                //
                // Messages are serialized into a transport-specific transmit buffer. Depending on the size of this buffer,
                // MANY messages may be buffered, however, they haven't yet been sent to the server. The server must call
                // request() to pull a buffered message from the client.
                //
                // Note: the onReadyHandler's invocation is serialized on the same thread pool as the incoming
                // StreamObserver'sonNext(), onError(), and onComplete() handlers. Blocking the onReadyHandler will prevent
                // additional messages from being processed by the incoming StreamObserver. The onReadyHandler must return
                // in a timely manor or else message processing throughput will suffer.
                requestStream.setOnReadyHandler(new Runnable() {
                    // An iterator is used so we can pause and resume iteration of the request data.

                    @Override
                    public void run() {
                        // Start generating values from where we left off on a non-gRPC thread.
                        while (requestStream.isReady()) {
                            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                                System.out.println("Streaming data to server: name" + i);
                                HelloRequest request = HelloRequest.newBuilder().setFirstName("name " + i).build();
                                requestStream.onNext(request);

                            }
                            requestStream.onCompleted();

                        }

                    }
                });
            }

            @Override
            public void onNext(HelloResponse value) {
                // Signal the sender to send one message.
                System.out.println("get data streaming bidirectional " + value.getGreeting());
                requestStream.request(1);
            }

            @Override
            public void onError(Throwable t) {
                requestStream.request(1);

                System.out.println(t.getStackTrace());
            }

            @Override
            public void onCompleted() {
            }
        };

        stub.helloStreamingBidirectional(clientResponseObserver);
    }

}
