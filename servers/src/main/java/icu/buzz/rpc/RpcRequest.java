package icu.buzz.rpc;

public class RpcRequest {
    private String serviceName;
    private String methodName;
    private Object[] params;

    // reserved for kryo
    public RpcRequest() {
    }

    public RpcRequest(String serviceName, String methodName, Object[] params) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.params = params;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
