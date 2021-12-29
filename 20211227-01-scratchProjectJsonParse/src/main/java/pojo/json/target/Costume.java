package pojo.json.target;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

public class Costume
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
    @JSONField(name = "bitmapResolution")
    private double bitmapResolution;

    @Getter
    @Setter
    @JSONField(name = "md5ext")
    private String md5ext;

    @Getter
    @Setter
    @JSONField(name = "dataFormat")
    private String dataFormat;

    @Getter
    @Setter
    @JSONField(name = "rotationCenterX")
    private double rotationCenterX;

    @Getter
    @Setter
    @JSONField(name = "rotationCenterY")
    private double rotationCenterY;

}
