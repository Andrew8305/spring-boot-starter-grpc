package com.quancheng.starter.grpc.internal;

import java.util.Map;

import com.google.gson.Gson;
import com.quancheng.starter.grpc.GrpcConstants;
import com.quancheng.starter.grpc.RpcContext;

import io.grpc.ForwardingServerCall.SimpleForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

public class GRpcHeaderServerInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, final Metadata headers,
                                                      ServerCallHandler<ReqT, RespT> next) {
        copyMetadataToThreadLocal(headers);
        return next.startCall(new SimpleForwardingServerCall<ReqT, RespT>(call) {

            @Override
            public void sendHeaders(Metadata responseHeaders) {
                super.sendHeaders(responseHeaders);
            }
        }, headers);
    }

    private void copyMetadataToThreadLocal(Metadata headers) {
        byte[] attachmentsBytes = headers.get(GrpcConstants.GRPC_CONTEXT_ATTACHMENTS);
        byte[] valuesByte = headers.get(GrpcConstants.GRPC_CONTEXT_VALUES);
        try {

            Map<String, String> attachments = new Gson().fromJson(new String(attachmentsBytes), Map.class);
            Map<String, Object> values = new Gson().fromJson(new String(valuesByte), Map.class);
            RpcContext.getContext().setAttachments(attachments);

            for (Map.Entry<String, Object> entry : values.entrySet()) {
                RpcContext.getContext().set(entry.getKey(), entry.getValue());
            }
        } catch (Throwable e) {
        }
    }

}
