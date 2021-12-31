package com.example.demo.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

public class Result
{
    // 对应七种类型flow control、 data representation abstraction、 user interactivity、
    // synchronization、 parallelism、 logic
    @Getter
    @Setter
    @JSONField(name = "name", deserialize = false)
    private String name;

    @Getter
    @Setter
    @JSONField(name = "score", deserialize = false)
    private int score;

    @Getter
    @Setter
    @JSONField(name = "max_score", deserialize = false)
    private int maxScore;

    @Getter
    @Setter
    @JSONField(name = "comments", deserialize = false)
    private Comments comments;

    public Result(String name, int score, int maxScore)
    {
        this.name     = name;
        this.score    = score;
        this.maxScore = maxScore;
    }
}
