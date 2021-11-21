package com.example.library.api.app.dao;

import java.util.Optional;

public class ResultHolder<T> {
    private T result;

    public void setResult(T result) {
        this.result = result;
    }

    public boolean isPresent() {
        return result != null;
    }

    public Optional<T> getResultOptional() {
        return result == null ? Optional.empty() : Optional.of(result);
    }
}
