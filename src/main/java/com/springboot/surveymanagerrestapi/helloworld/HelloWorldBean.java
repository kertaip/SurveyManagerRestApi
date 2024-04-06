package com.springboot.surveymanagerrestapi.helloworld;

public class HelloWorldBean {
    private String message;
    public HelloWorldBean(String message) {
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "HelloWorldBean{" +
                "message='" + message + '\'' +
                '}';
    }
}
