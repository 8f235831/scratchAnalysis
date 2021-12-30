package com.example.demo.pojo.json.target;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class Block
{
    @Getter
    @Setter
    @JSONField(name = "opcode")
    private String opcode;

    @Getter
    @Setter
    @JSONField(name = "next")
    private String next;

    @Getter
    @Setter
    @JSONField(name = "parent")
    private String parent;

    @Getter
    @Setter
    @JSONField(name = "inputs")
    private Map<String, Object[]> inputs;

    @Getter
    @Setter
    @JSONField(name = "fields")
    private Map<String, Object[]> fields;

    @Getter
    @Setter
    @JSONField(name = "shadow")
    private boolean shadow;

    @Getter
    @Setter
    @JSONField(name = "topLevel")
    private boolean topLevel;

    @Getter
    @Setter
    @JSONField(name = "mutation")
    private Mutation mutation;
}
