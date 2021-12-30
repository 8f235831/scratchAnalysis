package com.example.demo.pojo;

import lombok.Getter;
import lombok.Setter;
import com.example.demo.pojo.json.target.Block;
import com.example.demo.util.Analysis;

import java.util.LinkedList;
import java.util.List;

public class Operation
{
    public static final int OPCODE_NUMBER = 34;
    public static final int OPCODE_DEFAULT_ = 0;
    public static final int OPCODE_EVENT_WHENFLAGCLICKED = 1;
    public static final int OPCODE_EVENT_WHENBACKDROPSWITCHESTO = 2;
    public static final int OPCODE_EVENT_WHENTHISSPRITECLICKED = 3;
    public static final int OPCODE_EVENT_WHENKEYPRESSED = 4;
    public static final int OPCODE_EVENT_WHENGREATERTHAN = 5;
    public static final int OPCODE_EVENT_WHENBROADCASTRECEIVED = 6;
    public static final int OPCODE_EVENT_BROADCAST = 7;
    public static final int OPCODE_EVENT_BROADCASTANDWAIT = 8;
    public static final int OPCODE_EVENT_OTHERS_ = 9;
    public static final int OPCODE_CONTROL_REPEAT = 10;
    public static final int OPCODE_CONTROL_FOREVER = 11;
    public static final int OPCODE_CONTROL_IF = 12;
    public static final int OPCODE_CONTROL_IF_ELSE = 13;
    public static final int OPCODE_CONTROL_WAIT_UNTIL = 14;
    public static final int OPCODE_CONTROL_REPEAT_UNTIL = 15;
    public static final int OPCODE_CONTROL_STOP = 16;
    public static final int OPCODE_CONTROL_START_AS_CLONE = 17;
    public static final int OPCODE_CONTROL_WAIT = 18;
    public static final int OPCODE_SENSING_TOUCHINGOBJECT = 19;
    public static final int OPCODE_SENSING_TOUCHINGOBJECTMENU = 20;
    public static final int OPCODE_SENSING_TOUCHINGCOLOR = 21;
    public static final int OPCODE_SENSING_COLORISTOUCHINGCOLOR = 22;
    public static final int OPCODE_SENSING_ASKANDWAIT = 23;
    public static final int OPCODE_SENSING_KEYPRESSED = 24;
    public static final int OPCODE_SENSING_KEYOPTIONS = 25;
    public static final int OPCODE_SENSING_MOUSEDOWN = 26;
    public static final int OPCODE_SENSING_SETDRAGMODE = 27;
    public static final int OPCODE_SENSING_RESETTIMER = 28;
    public static final int OPCODE_OPERATOR_ = 29;
    // 将variable 和 list 分开
    public static final int OPCODE_DATA_VARIABLE = 30;
    public static final int OPCODE_DATA_LIST = 31;
    public static final int OPCODE_PROCEDURES_ = 32;
    // 新增
    public static final int OPCODE_MOTION_ = 33;

    @Getter
    @Setter
    private String id;

    @Getter
    private int opcodeType;

    @Getter
    @Setter
    private Block block;

    @Getter
    @Setter
    private Operation next;

    @Getter
    @Setter
    private Operation parent;

    @Getter
    @Setter
    private List<Operation> refer;

    @Getter
    @Setter
    private List<Operation> referred;

    public Operation(String id, Block block)
    {
        this.id         = id;
        this.block      = block;
        this.refer      = new LinkedList<>();
        this.referred   = new LinkedList<>();
        // 根据 opcode 来判断其类型
        this.opcodeType = Analysis.judgeOpcode(this.block.getOpcode());
    }

    @Override
    public String toString()
    {
        return this.id + " - " + this.block.getOpcode();
    }
}