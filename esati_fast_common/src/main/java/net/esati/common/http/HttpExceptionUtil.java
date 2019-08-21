package net.esati.common.http;

public class HttpExceptionUtil extends RuntimeException {
    private static final long serialVersionUID = 260136951169545885L;

    HttpExceptionUtil(String message) {
        super(message);
    }
}