package com.example.demo.pojo;

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
    private List<Result> results = new LinkedList<Result>()
    {{
        add(new Result("Flow control", 0, 3));
        add(new Result("Data representation", 0, 3));
        add(new Result("Abstraction", 0, 3));
        add(new Result("User interactivity", 0, 3));
        add(new Result("Synchronization", 0, 3));
        add(new Result("Parallelism", 0, 3));
        add(new Result("Logic", 0, 3));
    }};

    // 设置 opcode 的分数
    public void changeScore(String opcode, int score)
    {
        for(int i = 0; i < results.size(); ++i)
        {
            if(results.get(i).getName().equals(opcode) &&
               score > results.get(i).getScore())
            {
                results.get(i).setScore(score);
            }
        }
    }
}

