package myNetty.protocol;

/**
 * 相应请求
 */
public class RpcResponse {

    private String requestId;
    private Object data;
    // 0=success -1=fail
    private int status;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}