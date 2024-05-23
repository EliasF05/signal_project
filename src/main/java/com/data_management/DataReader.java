package com.data_management;

public interface DataReader {
    void connect(String uri);
    void disconnect();
    void onMessage(String message);
}
