package com.digitalminds.dmssevent.util;


import com.google.gson.JsonObject;

import java.util.List;

public class SaveStatusRes {
    private boolean status;
    private JsonObject Message;

    // Getters and Setters
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public JsonObject getMessage() {
        return Message;
    }

    public void setMessage(JsonObject Message) {
        this.Message = Message;
    }

  /*  public static class Message {
        private String Message;
        private List<String> details;
        private int code;

        // Getters and Setters
        public String getMessage() {
            return Message;
        }

        public void setMessage(String Message) {
            this.Message = Message;
        }

        public List<String> getDetails() {
            return details;
        }

        public void setDetails(List<String> details) {
            this.details = details;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }*/
}
