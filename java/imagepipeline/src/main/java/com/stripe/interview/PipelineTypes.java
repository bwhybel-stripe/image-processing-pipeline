package com.stripe.interview;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PipelineTypes {

    // Enum for result status remains the same
    public enum PipelineResultStatus {
        SUCCESS,
        FILE_NOT_FOUND,
        CONFIG_ERROR,
        PROCESSING_ERROR,
        SAVE_ERROR,
        UNSUPPORTED_OPERATION
    }

    public static final class OperationConfig {
        private final String name;
        private final Map<String, Integer> parameters;

        public OperationConfig(String name, Map<String, Integer> parameters) {
            this.name = name;
            this.parameters = parameters;
        }

        public String getName() {
            return name;
        }

        public Map<String, Integer> getParameters() {
            return new HashMap<>(parameters);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OperationConfig that = (OperationConfig) o;
            return name.equals(that.name) &&
                    parameters.equals(that.parameters);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, parameters);
        }

        @Override
        public String toString() {
            return "OperationConfig{" +
                    "name='" + name + '\'' +
                    ", parameters=" + parameters +
                    '}';
        }
    }


    // PipelineResult as a traditional class
    public static final class PipelineResult {
        private final PipelineResultStatus status;
        private final String message;

        public PipelineResult(PipelineResultStatus status, String message) {
            if (status == null) {
                throw new NullPointerException("PipelineResultStatus cannot be null");
            }
            this.status = status;
            this.message = (message != null) ? message : ""; // Ensure message is not null
        }

        public PipelineResultStatus getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PipelineResult that = (PipelineResult) o;
            return status == that.status &&
                    message.equals(that.message);
        }

        @Override
        public int hashCode() {
            return Objects.hash(status, message);
        }

        @Override
        public String toString() {
            return "PipelineResult{" +
                    "status=" + status +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}
