package pojo.json.target;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

public class Sound
{

    @Getter
    @Setter
    @JSONField(name = "assetId")
    private String assetId;

    @Getter
    @Setter
    @JSONField(name = "name")
    private String name;

    @Getter
    @Setter
    @JSONField(name = "dataFormat")
    private String dataFormat;

    @Getter
    @Setter
    @JSONField(name = "format")
    private String format;

    @Getter
    @Setter
    @JSONField(name = "rate")
    private int rate;

    @Getter
    @Setter
    @JSONField(name = "sampleCount")
    private int sampleCount;

    @Getter
    @Setter
    @JSONField(name = "md5ext")
    private String md5ext;
}
