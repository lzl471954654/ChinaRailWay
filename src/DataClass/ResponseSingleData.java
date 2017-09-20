package DataClass;

import java.io.Serializable;

public class ResponseSingleData<T> implements Serializable {
    long code;
    T data;

    public ResponseSingleData(long code, T data) {
        this.code = code;
        this.data = data;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
