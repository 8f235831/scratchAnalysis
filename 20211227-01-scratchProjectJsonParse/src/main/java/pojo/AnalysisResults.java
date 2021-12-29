package pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AnalysisResults
{
    @Getter
    @Setter
    @JSONField(name = "sourceHash", deserialize = false)
    private String sourceHash;

    @Getter
    @Setter
    @JSONField(name = "results", deserialize = false)
    private List<Result> results;
}

class Result
{
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
}

class Comments
{
    @Getter
    @Setter
    @JSONField(name = "used", deserialize = false)
    private String[] used;

    @Getter
    @Setter
    @JSONField(name = "count", deserialize = false)
    private int[] count;
}
