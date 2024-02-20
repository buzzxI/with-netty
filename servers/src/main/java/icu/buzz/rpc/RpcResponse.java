package icu.buzz.rpc;

public class RpcResponse {
    private Object rst;

    public RpcResponse(Object rst) {
        this.rst = rst;
    }

    public Object getRst() {
        return rst;
    }

    public void setRst(Object rst) {
        this.rst = rst;
    }
}
