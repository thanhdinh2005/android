package com.android.chatapp.discovery.repository;

public interface Callback<T> {
    void onSuccess(T result);
    void onError(String errorMessage);
}