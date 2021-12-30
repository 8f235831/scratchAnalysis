package pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

public class AnalysisResults
{
    @Getter
    @Setter
    @JSONField(name = "sourceHash", deserialize = false)
    private String sourceHash;

    // 存放结果的列表 默认得分为0 满分为3
    @Getter
    @Setter
    @JSONField(name = "results", deserialize = false)
    private List<Result> results = new LinkedList<Result>() {{
       add(new Result("Flow control", 0, 3));
       add(new Result("Data representation", 0, 3));
       add(new Result("Abstraction", 0, 3));
       add(new Result("User interactivity", 0, 3));
       add(new Result("Synchronization", 0, 3));
       add(new Result("Parallelism", 0, 3));
       add(new Result("Logic", 0, 3));
    }};

    // 设置 opcode 的分数
    public void setScore(String opcode, int score) {
        for (int i = 0; i < results.size(); ++i) {
            if (results.get(i).getName().equals(opcode)) {
                results.get(i).setScore(score);
            }
        }
    }
}

class Result
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

    public Result(String name, int score, int maxScore) {
        this.name = name;
        this.score = score;
        this.maxScore = maxScore;
    }
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
