package com.faber.service;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.4.0)",
    comments = "Source: HelloService.proto")
public final class HelloServiceGrpc {

  private HelloServiceGrpc() {}

  public static final String SERVICE_NAME = "com.faber.service.HelloService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.faber.service.HelloRequest,
      com.faber.service.HelloResponse> METHOD_HELLO =
      io.grpc.MethodDescriptor.<com.faber.service.HelloRequest, com.faber.service.HelloResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.faber.service.HelloService", "hello"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.faber.service.HelloRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.faber.service.HelloResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.faber.service.HelloRequest,
      com.faber.service.HelloResponse> METHOD_HELLO_STREAMING =
      io.grpc.MethodDescriptor.<com.faber.service.HelloRequest, com.faber.service.HelloResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
          .setFullMethodName(generateFullMethodName(
              "com.faber.service.HelloService", "helloStreaming"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.faber.service.HelloRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.faber.service.HelloResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.faber.service.HelloRequest,
      com.faber.service.HelloResponse> METHOD_HELLO_STREAMING_BIDIRECTIONAL =
      io.grpc.MethodDescriptor.<com.faber.service.HelloRequest, com.faber.service.HelloResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
          .setFullMethodName(generateFullMethodName(
              "com.faber.service.HelloService", "helloStreamingBidirectional"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.faber.service.HelloRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.faber.service.HelloResponse.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static HelloServiceStub newStub(io.grpc.Channel channel) {
    return new HelloServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static HelloServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new HelloServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static HelloServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new HelloServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class HelloServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void hello(com.faber.service.HelloRequest request,
        io.grpc.stub.StreamObserver<com.faber.service.HelloResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_HELLO, responseObserver);
    }

    /**
     */
    public void helloStreaming(com.faber.service.HelloRequest request,
        io.grpc.stub.StreamObserver<com.faber.service.HelloResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_HELLO_STREAMING, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.faber.service.HelloRequest> helloStreamingBidirectional(
        io.grpc.stub.StreamObserver<com.faber.service.HelloResponse> responseObserver) {
      return asyncUnimplementedStreamingCall(METHOD_HELLO_STREAMING_BIDIRECTIONAL, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_HELLO,
            asyncUnaryCall(
              new MethodHandlers<
                com.faber.service.HelloRequest,
                com.faber.service.HelloResponse>(
                  this, METHODID_HELLO)))
          .addMethod(
            METHOD_HELLO_STREAMING,
            asyncServerStreamingCall(
              new MethodHandlers<
                com.faber.service.HelloRequest,
                com.faber.service.HelloResponse>(
                  this, METHODID_HELLO_STREAMING)))
          .addMethod(
            METHOD_HELLO_STREAMING_BIDIRECTIONAL,
            asyncBidiStreamingCall(
              new MethodHandlers<
                com.faber.service.HelloRequest,
                com.faber.service.HelloResponse>(
                  this, METHODID_HELLO_STREAMING_BIDIRECTIONAL)))
          .build();
    }
  }

  /**
   */
  public static final class HelloServiceStub extends io.grpc.stub.AbstractStub<HelloServiceStub> {
    private HelloServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private HelloServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HelloServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new HelloServiceStub(channel, callOptions);
    }

    /**
     */
    public void hello(com.faber.service.HelloRequest request,
        io.grpc.stub.StreamObserver<com.faber.service.HelloResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_HELLO, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void helloStreaming(com.faber.service.HelloRequest request,
        io.grpc.stub.StreamObserver<com.faber.service.HelloResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(METHOD_HELLO_STREAMING, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.faber.service.HelloRequest> helloStreamingBidirectional(
        io.grpc.stub.StreamObserver<com.faber.service.HelloResponse> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(METHOD_HELLO_STREAMING_BIDIRECTIONAL, getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class HelloServiceBlockingStub extends io.grpc.stub.AbstractStub<HelloServiceBlockingStub> {
    private HelloServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private HelloServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HelloServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new HelloServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.faber.service.HelloResponse hello(com.faber.service.HelloRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_HELLO, getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<com.faber.service.HelloResponse> helloStreaming(
        com.faber.service.HelloRequest request) {
      return blockingServerStreamingCall(
          getChannel(), METHOD_HELLO_STREAMING, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class HelloServiceFutureStub extends io.grpc.stub.AbstractStub<HelloServiceFutureStub> {
    private HelloServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private HelloServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HelloServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new HelloServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.faber.service.HelloResponse> hello(
        com.faber.service.HelloRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_HELLO, getCallOptions()), request);
    }
  }

  private static final int METHODID_HELLO = 0;
  private static final int METHODID_HELLO_STREAMING = 1;
  private static final int METHODID_HELLO_STREAMING_BIDIRECTIONAL = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final HelloServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(HelloServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_HELLO:
          serviceImpl.hello((com.faber.service.HelloRequest) request,
              (io.grpc.stub.StreamObserver<com.faber.service.HelloResponse>) responseObserver);
          break;
        case METHODID_HELLO_STREAMING:
          serviceImpl.helloStreaming((com.faber.service.HelloRequest) request,
              (io.grpc.stub.StreamObserver<com.faber.service.HelloResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_HELLO_STREAMING_BIDIRECTIONAL:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.helloStreamingBidirectional(
              (io.grpc.stub.StreamObserver<com.faber.service.HelloResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class HelloServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.faber.service.HelloServiceOuterClass.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (HelloServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new HelloServiceDescriptorSupplier())
              .addMethod(METHOD_HELLO)
              .addMethod(METHOD_HELLO_STREAMING)
              .addMethod(METHOD_HELLO_STREAMING_BIDIRECTIONAL)
              .build();
        }
      }
    }
    return result;
  }
}
