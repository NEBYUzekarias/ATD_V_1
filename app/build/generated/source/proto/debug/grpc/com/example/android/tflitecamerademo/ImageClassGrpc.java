package com.example.android.tflitecamerademo;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 * Interface exported by the server.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.20.0)",
    comments = "Source: route_guide.proto")
public final class ImageClassGrpc {

  private ImageClassGrpc() {}

  public static final String SERVICE_NAME = "tflite.ImageClass";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.example.android.tflitecamerademo.GrpcRequest,
      com.example.android.tflitecamerademo.GrpcReply> getGetStageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getStage",
      requestType = com.example.android.tflitecamerademo.GrpcRequest.class,
      responseType = com.example.android.tflitecamerademo.GrpcReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.android.tflitecamerademo.GrpcRequest,
      com.example.android.tflitecamerademo.GrpcReply> getGetStageMethod() {
    io.grpc.MethodDescriptor<com.example.android.tflitecamerademo.GrpcRequest, com.example.android.tflitecamerademo.GrpcReply> getGetStageMethod;
    if ((getGetStageMethod = ImageClassGrpc.getGetStageMethod) == null) {
      synchronized (ImageClassGrpc.class) {
        if ((getGetStageMethod = ImageClassGrpc.getGetStageMethod) == null) {
          ImageClassGrpc.getGetStageMethod = getGetStageMethod = 
              io.grpc.MethodDescriptor.<com.example.android.tflitecamerademo.GrpcRequest, com.example.android.tflitecamerademo.GrpcReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "tflite.ImageClass", "getStage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  com.example.android.tflitecamerademo.GrpcRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  com.example.android.tflitecamerademo.GrpcReply.getDefaultInstance()))
                  .build();
          }
        }
     }
     return getGetStageMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ImageClassStub newStub(io.grpc.Channel channel) {
    return new ImageClassStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ImageClassBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ImageClassBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ImageClassFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ImageClassFutureStub(channel);
  }

  /**
   * <pre>
   * Interface exported by the server.
   * </pre>
   */
  public static abstract class ImageClassImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * A simple RPC.
     * Obtains the feature at a given position.
     * A feature with an empty name is returned if there's no feature at the given
     * position.
     * </pre>
     */
    public void getStage(com.example.android.tflitecamerademo.GrpcRequest request,
        io.grpc.stub.StreamObserver<com.example.android.tflitecamerademo.GrpcReply> responseObserver) {
      asyncUnimplementedUnaryCall(getGetStageMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetStageMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.example.android.tflitecamerademo.GrpcRequest,
                com.example.android.tflitecamerademo.GrpcReply>(
                  this, METHODID_GET_STAGE)))
          .build();
    }
  }

  /**
   * <pre>
   * Interface exported by the server.
   * </pre>
   */
  public static final class ImageClassStub extends io.grpc.stub.AbstractStub<ImageClassStub> {
    private ImageClassStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ImageClassStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ImageClassStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ImageClassStub(channel, callOptions);
    }

    /**
     * <pre>
     * A simple RPC.
     * Obtains the feature at a given position.
     * A feature with an empty name is returned if there's no feature at the given
     * position.
     * </pre>
     */
    public void getStage(com.example.android.tflitecamerademo.GrpcRequest request,
        io.grpc.stub.StreamObserver<com.example.android.tflitecamerademo.GrpcReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetStageMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * Interface exported by the server.
   * </pre>
   */
  public static final class ImageClassBlockingStub extends io.grpc.stub.AbstractStub<ImageClassBlockingStub> {
    private ImageClassBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ImageClassBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ImageClassBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ImageClassBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * A simple RPC.
     * Obtains the feature at a given position.
     * A feature with an empty name is returned if there's no feature at the given
     * position.
     * </pre>
     */
    public com.example.android.tflitecamerademo.GrpcReply getStage(com.example.android.tflitecamerademo.GrpcRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetStageMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * Interface exported by the server.
   * </pre>
   */
  public static final class ImageClassFutureStub extends io.grpc.stub.AbstractStub<ImageClassFutureStub> {
    private ImageClassFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ImageClassFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ImageClassFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ImageClassFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * A simple RPC.
     * Obtains the feature at a given position.
     * A feature with an empty name is returned if there's no feature at the given
     * position.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.android.tflitecamerademo.GrpcReply> getStage(
        com.example.android.tflitecamerademo.GrpcRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetStageMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_STAGE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ImageClassImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ImageClassImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_STAGE:
          serviceImpl.getStage((com.example.android.tflitecamerademo.GrpcRequest) request,
              (io.grpc.stub.StreamObserver<com.example.android.tflitecamerademo.GrpcReply>) responseObserver);
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
        default:
          throw new AssertionError();
      }
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ImageClassGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .addMethod(getGetStageMethod())
              .build();
        }
      }
    }
    return result;
  }
}
