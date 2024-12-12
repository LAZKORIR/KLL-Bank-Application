package edu.miu.cs425.kllbankingsolution.dto;

public class Response {

    private String responseCode;
    private String responseMessage;

    public Response() {
    }

    public Response(String responseCode, String responseMessage) {}

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
