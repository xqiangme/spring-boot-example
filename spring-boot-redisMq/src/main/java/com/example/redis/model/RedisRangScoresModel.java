package com.example.redis.model;

/**
 * @author mengqiang
 */
public class RedisRangScoresModel<T> {
    private Double score;
    private T obj;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }


    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

}