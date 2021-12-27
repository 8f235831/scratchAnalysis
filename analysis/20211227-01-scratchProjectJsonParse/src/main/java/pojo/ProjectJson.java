package pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

public class ProjectJson
{
    @Getter
    @Setter
    @JSONField(name="targets")
    private Target[] targets;

    @Getter
    @Setter
    @JSONField(name="monitors")
    private Monitor[] monitors;

    @Getter
    @Setter
    @JSONField(name="extensions")
    private String[] extensions;

    @Getter
    @Setter
    @JSONField(name="meta")
    private Meta meta;
}
