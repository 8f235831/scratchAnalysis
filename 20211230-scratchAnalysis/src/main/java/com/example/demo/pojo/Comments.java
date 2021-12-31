package com.example.demo.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

public class Comments
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
