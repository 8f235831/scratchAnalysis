package util;

import pojo.json.*;
import pojo.json.target.*;
import pojo.Operation;

import java.util.*;

public class Analysis
{
    private Analysis()
    {

    }

    public static Operation[][] rebuildTargets(Target[] targets)
    {
        LinkedList<Operation[]> resultList = new LinkedList<>();
        for(Target target : targets)
        {
            resultList.add(rebuildTarget(target));
        }
        return resultList.toArray(new Operation[0][]);
    }

    public static Operation[] rebuildTarget(Target target)
    {
        Map<String, Block> blocks = target.getBlocks();
        LinkedList<String[]> parentRelationships = new LinkedList<>();
        LinkedList<String[]> nextRelationships = new LinkedList<>();
        LinkedList<String[]> referRelationships = new LinkedList<>();
        for(String id : blocks.keySet())
        {
            Block block = blocks.get(id);
            if(block.getParent() != null)
            {
                parentRelationships.add(new String[]{id, block.getParent()});
            }
            if(block.getNext() != null)
            {
                nextRelationships.add(new String[]{id, block.getNext()});
            }
            Map<String, Object[]> inputs = block.getInputs();
            LinkedList<String> referred = new LinkedList<>();
            for(String paramName : inputs.keySet())
            {
                Object[] paramValue = inputs.get(paramName);
                if(paramValue.length >= 2 &&
                   paramValue[1].getClass().equals(String.class) &&
                   ((String) paramValue[1]).length() == 20)
                {
                    referred.add((String) paramValue[1]);
                }
            }
            if(!referred.isEmpty())
            {
                referred.addFirst(id);
                referRelationships.add(referred.toArray(new String[0]));
            }

        }
        Map<String, Operation> result = new HashMap<>();
        for(String id : blocks.keySet())
        {
            Block block = blocks.get(id);
            Operation operation = new Operation(id, block);
            result.put(id, operation);
        }
        for(String[] parentRelationship : parentRelationships)
        {
            Operation from = result.get(parentRelationship[0]);
            Operation to = result.get(parentRelationship[1]);
            from.setParent(to);
        }
        for(String[] nextRelationship : nextRelationships)
        {
            Operation from = result.get(nextRelationship[0]);
            Operation to = result.get(nextRelationship[1]);
            from.setNext(to);
        }
        for(String[] referRelationship : referRelationships)
        {
            Operation from = result.get(referRelationship[0]);
            for(int i = 1; i < referRelationship.length; i++)
            {
                Operation to = result.get(referRelationship[1]);
                from.getRefer().add(to);
                to.getReferred().add(from);
            }
        }
        for(String id : result.keySet())
        {
            Operation operation = result.get(id);
            operation.setRefer(new ArrayList<>(operation.getRefer()));
            operation.setReferred(new ArrayList<>(operation.getReferred()));
        }
        return result.values().toArray(new Operation[0]);
    }

    public static String analyse(String source)
    {
        // TODO 需要在此实现具体的分析函数。
        return null;
    }
}
