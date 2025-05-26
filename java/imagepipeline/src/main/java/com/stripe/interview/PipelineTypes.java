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

    // OperationConfig as a traditional class
    public static final class OperationConfig { // Often made final for data classes
        private final String name;
        private final Map<String, Object> parameters; // e.g., {"factor": 1.5f, "width": 100}

        public OperationConfig(String name, Map<String, Object> parameters) {
            if (name == null) {
                throw new NullPointerException("Operation name cannot be null");
            }
            this.name = name;
            // Store an unmodifiable copy of the parameters map to ensure immutability
            this.parameters = (parameters != null) ? Collections.unmodifiableMap(new HashMap<>(parameters)) : Collections.emptyMap();
        }

        public String getName() {
            return name;
        }

        public Map<String, Object> getParameters() {
            // The internal map is already unmodifiable, so we can return it directly.
            // If we hadn't made an unmodifiable copy in the constructor,
            // we'd return new HashMap<>(parameters) here.
            return parameters;
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
