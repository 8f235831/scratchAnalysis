package pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import pojo.target.Block;
import pojo.target.Costume;
import pojo.target.Sound;

import java.util.Map;

public class Target
{
    @Getter
    @Setter
    @JSONField(name = "isStage")
    private boolean isStage;

    @Getter
    @Setter
    @JSONField(name = "name")
    private String name;

    @Getter
    @Setter
    @JSONField(name = "variables")
    private Map<String, Object[]> variables;

    @Getter
    @Setter
    @JSONField(name = "lists")
    private Object lists;

    @Getter
    @Setter
    @JSONField(name = "broadcasts")
    private Map<String, String> broadcasts;

    @Getter
    @Setter
    @JSONField(name = "blocks")
    private Map<String, Block> blocks;

    @Getter
    @Setter
    @JSONField(name = "comments")
    private Object comments;

    @Getter
    @Setter
    @JSONField(name = "currentCostume")
    private int currentCostume;

    @Getter
    @Setter
    @JSONField(name = "costumes")
    private Costume[] costumes;

    @Getter
    @Setter
    @JSONField(name = "sounds")
    private Sound[] sounds;

    @Getter
    @Setter
    @JSONField(name = "volume")
    private int volume;

    @Getter
    @Setter
    @JSONField(name = "layerOrder")
    private int layerOrder;

    @Getter
    @Setter
    @JSONField(name = "tempo")
    private int tempo;

    @Getter
    @Setter
    @JSONField(name = "videoTransparency")
    private int videoTransparency;

    @Getter
    @Setter
    @JSONField(name = "videoState")
    private String videoState;

    @Getter
    @Setter
    @JSONField(name = "textToSpeechLanguage")
    private String textToSpeechLanguage;

    @Getter
    @Setter
    @JSONField(name = "visible")
    private boolean visible;

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
    @JSONField(name = "size")
    private double size;

    @Getter
    @Setter
    @JSONField(name = "direction")
    private double direction;

    @Getter
    @Setter
    @JSONField(name = "draggable")
    private boolean draggable;

    @Getter
    @Setter
    @JSONField(name = "rotationStyle")
    private String rotationStyle;
}
