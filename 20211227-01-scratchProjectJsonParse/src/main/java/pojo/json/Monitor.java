package pojo.json;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


public class Monitor
{
    @Getter
    @Setter
    @JSONField(name = "id")
    private String id;

    @Getter
    @Setter
    @JSONField(name = "mode")
    private String mode;
    @Getter
    @Setter
    @JSONField(name = "opcode")
    private String opcode;

    @Getter
    @Setter
    @JSONField(name = "params")
    private Map<String, String> params;

    @Getter
    @Setter
    @JSONField(name = "spriteName")
    private String spriteName;

    @Getter
    @Setter
    @JSONField(name = "value")
    private String value;

    @Getter
    @Setter
    @JSONField(name = "width")
    private double width;

    @Getter
    @Setter
    @JSONField(name = "height")
    private double height;

    @Getter
    @Setter
    @JSONField(name = "x")
    private double x;

    @Getter
    @Setter
    @JSONField(name = "y")
    private double y;

    @Getter
    @Setter
    @JSONField(name = "visible")
    private boolean visible;

    @Getter
    @Setter
    @JSONField(name = "sliderMin")
    private double sliderMin;

    @Getter
    @Setter
    @JSONField(name = "sliderMax")
    private double sliderMax;

    @Getter
    @Setter
    @JSONField(name = "isDiscrete")
    private boolean isDiscrete;

    public Monitor()
    {
    }
}
