package com.stevenkristian.tubes.UnitTesting;

import com.stevenkristian.tubes.model.Motor;

public interface Callback {
    void onSuccess(boolean value, Motor motor);
    void onError();
}
