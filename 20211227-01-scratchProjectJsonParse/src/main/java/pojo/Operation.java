package pojo;

import lombok.Getter;
import lombok.Setter;
import pojo.json.target.Block;

import java.util.LinkedList;
import java.util.List;

public class Operation
{
    @Getter
    @Setter
    private String id;

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
        this.id       = id;
        this.block    = block;
        this.refer    = new LinkedList<>();
        this.referred = new LinkedList<>();
    }

    @Override
    public String toString()
    {
        return this.id + " - " + this.block.getOpcode();
    }
}
