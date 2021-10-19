package com.hui.core.widget.dsbridge;

/**
 * Created by du on 16/12/31.
 * from https://github.com/wendux/DSBridge-Android
 */

public interface  CompletionHandler<T> {
    void complete(T retValue);
    void complete();
    void setProgressData(T value);
}
