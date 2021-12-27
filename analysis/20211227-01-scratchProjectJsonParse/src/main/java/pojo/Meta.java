package pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

public class Meta
{
    @Getter
    @Setter
    @JSONField(name = "semver")
    private String semver;

    @Getter
    @Setter
    @JSONField(name = "vm")
    private String vm;

    @Getter
    @Setter
    @JSONField(name = "agent")
    private String agent;

    public Meta()
    {
    }
}

