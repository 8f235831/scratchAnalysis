package pojo.target;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class Mutation
{
    @Getter
    @Setter
    @JSONField(name = "tagName")
    private String tagName;

    @Getter
    @Setter
    @JSONField(name = "fields")
    private Object[] children;

    @Getter
    @Setter
    @JSONField(name = "proccode")
    private String proccode;

    @Getter
    @Setter
    @JSONField(name = "argumentids")
    private String argumentids;

    @Getter
    @Setter
    @JSONField(name = "argumentnames")
    private String argumentnames;

    @Getter
    @Setter
    @JSONField(name = "argumentdefaults")
    private String argumentdefaults;

    @Getter
    @Setter
    @JSONField(name = "warp")
    private String warp;

    public Mutation()
    {
    }
}
