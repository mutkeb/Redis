package com.youkeda.app.service;

import com.youkeda.app.model.Result;

public interface SnappedUpService {
    /**
     * 抢购
     */
    Result<Boolean> snappedUp(Long id);
}
