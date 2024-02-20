package icu.buzz.rpc;

public class RpcResponse {
    private Object rst;
    private String msg;

    public RpcResponse() {
    }
    public RpcResponse(Object rst) {
        this.rst = rst;
        msg = null;
    }

    public Object getRst() {
        return rst;
    }

    public void setRst(Object rst) {
        this.rst = rst;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
