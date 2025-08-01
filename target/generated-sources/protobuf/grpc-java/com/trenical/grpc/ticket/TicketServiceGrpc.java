package com.trenical.grpc.ticket;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Servizio per la gestione dei ticket ferroviari
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.61.0)",
    comments = "Source: ticket_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class TicketServiceGrpc {

  private TicketServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "trenical.ticket.TicketService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.trenical.grpc.ticket.PreviewRequest,
      com.trenical.grpc.ticket.PreviewResponse> getGetTicketPreviewsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetTicketPreviews",
      requestType = com.trenical.grpc.ticket.PreviewRequest.class,
      responseType = com.trenical.grpc.ticket.PreviewResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.trenical.grpc.ticket.PreviewRequest,
      com.trenical.grpc.ticket.PreviewResponse> getGetTicketPreviewsMethod() {
    io.grpc.MethodDescriptor<com.trenical.grpc.ticket.PreviewRequest, com.trenical.grpc.ticket.PreviewResponse> getGetTicketPreviewsMethod;
    if ((getGetTicketPreviewsMethod = TicketServiceGrpc.getGetTicketPreviewsMethod) == null) {
      synchronized (TicketServiceGrpc.class) {
        if ((getGetTicketPreviewsMethod = TicketServiceGrpc.getGetTicketPreviewsMethod) == null) {
          TicketServiceGrpc.getGetTicketPreviewsMethod = getGetTicketPreviewsMethod =
              io.grpc.MethodDescriptor.<com.trenical.grpc.ticket.PreviewRequest, com.trenical.grpc.ticket.PreviewResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetTicketPreviews"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.trenical.grpc.ticket.PreviewRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.trenical.grpc.ticket.PreviewResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TicketServiceMethodDescriptorSupplier("GetTicketPreviews"))
              .build();
        }
      }
    }
    return getGetTicketPreviewsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TicketServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TicketServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TicketServiceStub>() {
        @java.lang.Override
        public TicketServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TicketServiceStub(channel, callOptions);
        }
      };
    return TicketServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TicketServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TicketServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TicketServiceBlockingStub>() {
        @java.lang.Override
        public TicketServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TicketServiceBlockingStub(channel, callOptions);
        }
      };
    return TicketServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TicketServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TicketServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TicketServiceFutureStub>() {
        @java.lang.Override
        public TicketServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TicketServiceFutureStub(channel, callOptions);
        }
      };
    return TicketServiceFutureStub.newStub(factory, channel);
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
    default void getTicketPreviews(com.trenical.grpc.ticket.PreviewRequest request,
        io.grpc.stub.StreamObserver<com.trenical.grpc.ticket.PreviewResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetTicketPreviewsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service TicketService.
   * <pre>
   * Servizio per la gestione dei ticket ferroviari
   * </pre>
   */
  public static abstract class TicketServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return TicketServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service TicketService.
   * <pre>
   * Servizio per la gestione dei ticket ferroviari
   * </pre>
   */
  public static final class TicketServiceStub
      extends io.grpc.stub.AbstractAsyncStub<TicketServiceStub> {
    private TicketServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TicketServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TicketServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Restituisce una lista di anteprime di biglietti compatibili con i criteri di ricerca
     * </pre>
     */
    public void getTicketPreviews(com.trenical.grpc.ticket.PreviewRequest request,
        io.grpc.stub.StreamObserver<com.trenical.grpc.ticket.PreviewResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetTicketPreviewsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service TicketService.
   * <pre>
   * Servizio per la gestione dei ticket ferroviari
   * </pre>
   */
  public static final class TicketServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<TicketServiceBlockingStub> {
    private TicketServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TicketServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TicketServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Restituisce una lista di anteprime di biglietti compatibili con i criteri di ricerca
     * </pre>
     */
    public com.trenical.grpc.ticket.PreviewResponse getTicketPreviews(com.trenical.grpc.ticket.PreviewRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTicketPreviewsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service TicketService.
   * <pre>
   * Servizio per la gestione dei ticket ferroviari
   * </pre>
   */
  public static final class TicketServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<TicketServiceFutureStub> {
    private TicketServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TicketServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TicketServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Restituisce una lista di anteprime di biglietti compatibili con i criteri di ricerca
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.trenical.grpc.ticket.PreviewResponse> getTicketPreviews(
        com.trenical.grpc.ticket.PreviewRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetTicketPreviewsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_TICKET_PREVIEWS = 0;

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
        case METHODID_GET_TICKET_PREVIEWS:
          serviceImpl.getTicketPreviews((com.trenical.grpc.ticket.PreviewRequest) request,
              (io.grpc.stub.StreamObserver<com.trenical.grpc.ticket.PreviewResponse>) responseObserver);
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
          getGetTicketPreviewsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.trenical.grpc.ticket.PreviewRequest,
              com.trenical.grpc.ticket.PreviewResponse>(
                service, METHODID_GET_TICKET_PREVIEWS)))
        .build();
  }

  private static abstract class TicketServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TicketServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.trenical.grpc.ticket.TicketServiceProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TicketService");
    }
  }

  private static final class TicketServiceFileDescriptorSupplier
      extends TicketServiceBaseDescriptorSupplier {
    TicketServiceFileDescriptorSupplier() {}
  }

  private static final class TicketServiceMethodDescriptorSupplier
      extends TicketServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    TicketServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (TicketServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TicketServiceFileDescriptorSupplier())
              .addMethod(getGetTicketPreviewsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
