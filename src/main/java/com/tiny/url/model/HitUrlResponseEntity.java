package com.tiny.url.model;

public class HitUrlResponseEntity {
    String status;
    Integer hitCount;

    public HitUrlResponseEntity(String status, Integer hitCount) {
        this.status = status;
        this.hitCount = hitCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getHitCount() {
        return hitCount;
    }

    public void setHitCount(Integer hitCount) {
        this.hitCount = hitCount;
    }
}
