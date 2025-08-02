package com.trenical.grpc.train;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Servizio per la gestione dei ticket ferroviari
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.61.0)",
    comments = "Source: train_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class TrainServiceGrpc {

  private TrainServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "trenical.train.TrainService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.trenical.grpc.train.TrainSearchRequest,
      com.trenical.grpc.train.TrainSearchResponse> getSearchTrainByFiltersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SearchTrainByFilters",
      requestType = com.trenical.grpc.train.TrainSearchRequest.class,
      responseType = com.trenical.grpc.train.TrainSearchResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.trenical.grpc.train.TrainSearchRequest,
      com.trenical.grpc.train.TrainSearchResponse> getSearchTrainByFiltersMethod() {
    io.grpc.MethodDescriptor<com.trenical.grpc.train.TrainSearchRequest, com.trenical.grpc.train.TrainSearchResponse> getSearchTrainByFiltersMethod;
    if ((getSearchTrainByFiltersMethod = TrainServiceGrpc.getSearchTrainByFiltersMethod) == null) {
      synchronized (TrainServiceGrpc.class) {
        if ((getSearchTrainByFiltersMethod = TrainServiceGrpc.getSearchTrainByFiltersMethod) == null) {
          TrainServiceGrpc.getSearchTrainByFiltersMethod = getSearchTrainByFiltersMethod =
              io.grpc.MethodDescriptor.<com.trenical.grpc.train.TrainSearchRequest, com.trenical.grpc.train.TrainSearchResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SearchTrainByFilters"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.trenical.grpc.train.TrainSearchRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.trenical.grpc.train.TrainSearchResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TrainServiceMethodDescriptorSupplier("SearchTrainByFilters"))
              .build();
        }
      }
    }
    return getSearchTrainByFiltersMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TrainServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TrainServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TrainServiceStub>() {
        @java.lang.Override
        public TrainServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TrainServiceStub(channel, callOptions);
        }
      };
    return TrainServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TrainServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TrainServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TrainServiceBlockingStub>() {
        @java.lang.Override
        public TrainServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TrainServiceBlockingStub(channel, callOptions);
        }
      };
    return TrainServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TrainServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TrainServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TrainServiceFutureStub>() {
        @java.lang.Override
        public TrainServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TrainServiceFutureStub(channel, callOptions);
        }
      };
    return TrainServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Servizio per la gestione dei ticket ferroviari
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * Restituisce una lista di anteprime di biglietti compatibili con i criteri di ricerca
     * </pre>
     */
    default void searchTrainByFilters(com.trenical.grpc.train.TrainSearchRequest request,
        io.grpc.stub.StreamObserver<com.trenical.grpc.train.TrainSearchResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSearchTrainByFiltersMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service TrainService.
   * <pre>
   * Servizio per la gestione dei ticket ferroviari
   * </pre>
   */
  public static abstract class TrainServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return TrainServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service TrainService.
   * <pre>
   * Servizio per la gestione dei ticket ferroviari
   * </pre>
   */
  public static final class TrainServiceStub
      extends io.grpc.stub.AbstractAsyncStub<TrainServiceStub> {
    private TrainServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TrainServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TrainServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Restituisce una lista di anteprime di biglietti compatibili con i criteri di ricerca
     * </pre>
     */
    public void searchTrainByFilters(com.trenical.grpc.train.TrainSearchRequest request,
        io.grpc.stub.StreamObserver<com.trenical.grpc.train.TrainSearchResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSearchTrainByFiltersMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service TrainService.
   * <pre>
   * Servizio per la gestione dei ticket ferroviari
   * </pre>
   */
  public static final class TrainServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<TrainServiceBlockingStub> {
    private TrainServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TrainServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TrainServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Restituisce una lista di anteprime di biglietti compatibili con i criteri di ricerca
     * </pre>
     */
    public com.trenical.grpc.train.TrainSearchResponse searchTrainByFilters(com.trenical.grpc.train.TrainSearchRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSearchTrainByFiltersMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service TrainService.
   * <pre>
   * Servizio per la gestione dei ticket ferroviari
   * </pre>
   */
  public static final class TrainServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<TrainServiceFutureStub> {
    private TrainServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TrainServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TrainServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Restituisce una lista di anteprime di biglietti compatibili con i criteri di ricerca
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.trenical.grpc.train.TrainSearchResponse> searchTrainByFilters(
        com.trenical.grpc.train.TrainSearchRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSearchTrainByFiltersMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEARCH_TRAIN_BY_FILTERS = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEARCH_TRAIN_BY_FILTERS:
          serviceImpl.searchTrainByFilters((com.trenical.grpc.train.TrainSearchRequest) request,
              (io.grpc.stub.StreamObserver<com.trenical.grpc.train.TrainSearchResponse>) responseObserver);
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

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getSearchTrainByFiltersMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.trenical.grpc.train.TrainSearchRequest,
              com.trenical.grpc.train.TrainSearchResponse>(
                service, METHODID_SEARCH_TRAIN_BY_FILTERS)))
        .build();
  }

  private static abstract class TrainServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TrainServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.trenical.grpc.train.TrainServiceProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TrainService");
    }
  }

  private static final class TrainServiceFileDescriptorSupplier
      extends TrainServiceBaseDescriptorSupplier {
    TrainServiceFileDescriptorSupplier() {}
  }

  private static final class TrainServiceMethodDescriptorSupplier
      extends TrainServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    TrainServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (TrainServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TrainServiceFileDescriptorSupplier())
              .addMethod(getSearchTrainByFiltersMethod())
              .build();
        }
      }
    }
    return result;
  }
}
