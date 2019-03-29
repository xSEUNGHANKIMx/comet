package io.comet.Model;

import java.util.Date;

public class SimpleRes {
    String message = "";
    int code = 0;
    String data = "";
    Date timestamp = null;

    SimpleRes(String message, int code, String data, Date timestamp) {
        this.message = message;
        this.code = code;
        this.data = data;
        this.timestamp = timestamp;
    }
}
