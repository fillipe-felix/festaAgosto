package com.pdv.util;

import java.util.ArrayList;
import java.util.List;

public class Response<T> {

    private T data;
    private List<String> errors;
    private String mensagens;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<String> getErrors() {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }


    public String getMensagens() {
        return mensagens;
    }

    public void setMensagens(String mensagens) {
        this.mensagens = mensagens;
    }

    @Override
    public String toString() {
        return "Response [data=" + data + ", errors=" + errors + "]";
    }
    public String toStringMessage() {
        return "Response [data=" + data + ", mensage=" + mensagens + "]";
    }

}
