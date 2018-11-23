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
import io.vertx.core.http.HttpServerResponse;
import java.util.Iterator;
import java.util.concurrent.ForkJoinPool;
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

        channel.awaitTermination(60, TimeUnit.MINUTES);//shutdown hangout
    }

    public static void blockingConnect() {
        //Connect to service by blocking method
        HelloServiceGrpc.HelloServiceBlockingStub stub = HelloServiceGrpc.newBlockingStub(channel);
        HelloResponse helloResponse = stub.hello(HelloRequest.newBuilder()
                .setFirstName("Baeldung")
                .setLastName("gRPC")
                .build());
        System.out.println("get blocking response: " + helloResponse.getGreeting());
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
        
        //Get multiple response
        Iterator<HelloResponse> helloResponses = stub.helloStreaming(HelloRequest.newBuilder()
                .setFirstName("Baeldung")
                .setLastName("gRPC")
                .build());
        
        //do sth with each response
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

}
