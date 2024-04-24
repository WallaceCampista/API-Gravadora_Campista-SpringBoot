package com.example.apigravadora.exception.error;

public class ApiErrorDetails {
    private long timestamp;
    private int status;
    private String title;
    private String detail;
    private String developerMessage;

    // Construtor, getters e setters aqui

    public static class Builder {
        private long timestamp;
        private int status;
        private String title;
        private String detail;
        private String developerMessage;

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public Builder developerMessage(String developerMessage) {
            this.developerMessage = developerMessage;
            return this;
        }

        public ApiErrorDetails build() {
            ApiErrorDetails errorDetails = new ApiErrorDetails();
            errorDetails.timestamp = this.timestamp;
            errorDetails.status = this.status;
            errorDetails.title = this.title;
            errorDetails.detail = this.detail;
            errorDetails.developerMessage = this.developerMessage;
            return errorDetails;
        }
    }
}
