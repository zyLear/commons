package com.zylear.commons.bean;

public class RequestHolder {
    private final static ThreadLocal<RequestObject> REQUEST_OBJECT = new ThreadLocal<>();

    public static void set(RequestObject requestObject) {
        REQUEST_OBJECT.set(requestObject);
    }

    public static RequestObject get() {
        return REQUEST_OBJECT.get();
    }

    public static void clear() {
        REQUEST_OBJECT.remove();
    }
}
