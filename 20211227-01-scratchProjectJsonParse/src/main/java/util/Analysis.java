package util;

import com.alibaba.fastjson.JSON;
import pojo.AnalysisResults;
import pojo.Operation;
import pojo.json.*;
import pojo.json.target.*;

import java.util.*;

public class Analysis
{
    private Analysis()
    {

    }

    public static AnalysisResults analyse(String source)
    {
        ProjectJson projectJson = JSON.parseObject(source, ProjectJson.class);
        Operation[][] totalOperations = rebuildTargets(
                projectJson.getTargets());
        int[] callCounter = new int[Operation.OPCODE_NUMBER];
        int[] stackCounter = new int[2];
        for(Operation[] operations : totalOperations)
        {
            for(Operation operation : operations)
            {
                callCounter[operation.getOpcodeType()]++;
            }
        }

        // TODO 需要在此实现具体的分析函数。
        return null;
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
                Operation to = result.get(referRelationship[i]);
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

    public static int judgeOpcode(String opcode)
    {
        if(opcode.contains("event_"))
        {
            return switch(opcode)
                    {
                        case "event_whenflagclicked" -> Operation.OPCODE_EVENT_WHENFLAGCLICKED;
                        case "event_whenbackdropswitchesto" -> Operation.OPCODE_EVENT_WHENBACKDROPSWITCHESTO;
                        case "event_whenthisspriteclicked" -> Operation.OPCODE_EVENT_WHENTHISSPRITECLICKED;
                        case "event_whenkeypressed" -> Operation.OPCODE_EVENT_WHENKEYPRESSED;
                        case "event_whengreaterthan" -> Operation.OPCODE_EVENT_WHENGREATERTHAN;
                        case "event_whenbroadcastreceived" -> Operation.OPCODE_EVENT_WHENBROADCASTRECEIVED;
                        case "event_broadcast" -> Operation.OPCODE_EVENT_BROADCAST;
                        case "event_broadcastandwait" -> Operation.OPCODE_EVENT_BROADCASTANDWAIT;
                        default -> Operation.OPCODE_EVENT_OTHERS_;
                    };
        }
        else if(opcode.contains("control_"))
        {
            return switch(opcode)
                    {
                        case "control_repeat" -> Operation.OPCODE_CONTROL_REPEAT;
                        case "control_forever" -> Operation.OPCODE_CONTROL_FOREVER;
                        case "control_if" -> Operation.OPCODE_CONTROL_IF;
                        case "control_if_else" -> Operation.OPCODE_CONTROL_IF_ELSE;
                        case "control_wait_until" -> Operation.OPCODE_CONTROL_WAIT_UNTIL;
                        case "control_repeat_until" -> Operation.OPCODE_CONTROL_REPEAT_UNTIL;
                        case "control_stop" -> Operation.OPCODE_CONTROL_STOP;
                        case "control_start_as_clone" -> Operation.OPCODE_CONTROL_START_AS_CLONE;
                        case "control_wait" -> Operation.OPCODE_CONTROL_WAIT;
                        default -> Operation.OPCODE_DEFAULT_;
                    };
        }
        else if(opcode.contains("sensing_"))
        {
            return switch(opcode)
                    {
                        case "sensing_touchingobject" -> Operation.OPCODE_SENSING_TOUCHINGOBJECT;
                        case "sensing_touchingobjectmenu" -> Operation.OPCODE_SENSING_TOUCHINGOBJECTMENU;
                        case "sensing_touchingcolor" -> Operation.OPCODE_SENSING_TOUCHINGCOLOR;
                        case "sensing_coloristouchingcolor" -> Operation.OPCODE_SENSING_COLORISTOUCHINGCOLOR;
                        case "sensing_askandwait" -> Operation.OPCODE_SENSING_ASKANDWAIT;
                        case "sensing_keypressed" -> Operation.OPCODE_SENSING_KEYPRESSED;
                        case "sensing_keyoptions" -> Operation.OPCODE_SENSING_KEYOPTIONS;
                        case "sensing_mousedown" -> Operation.OPCODE_SENSING_MOUSEDOWN;
                        case "sensing_setdragmode" -> Operation.OPCODE_SENSING_SETDRAGMODE;
                        case "sensing_resettimer" -> Operation.OPCODE_SENSING_RESETTIMER;
                        default -> Operation.OPCODE_DEFAULT_;
                    };
        }
        else if(opcode.contains("operator_"))
        {
            return Operation.OPCODE_OPERATOR_;
        }
        else if(opcode.contains("data_"))
        {
            return Operation.OPCODE_DATA_;
        }
        else if(opcode.contains("procedures_"))
        {
            return Operation.OPCODE_PROCEDURES_;
        }
        else
        {
            return Operation.OPCODE_DEFAULT_;
        }
    }


    public static void traverseOperations(
            Operation[] operations, int[] stackCounter
    )
    {
        LinkedList<Operation> roots = new LinkedList<>();
        for(Operation operation : operations)
        {
            if(operation.getParent() == null)
            {
                roots.add(operation);
            }
        }
        for(Operation operation : roots)
        {

            System.out.println("0 - " + operation);
            traverseOperation(operation, stackCounter, false, 0);
        }
    }

    private static void traverseOperation(
            Operation operation, int[] stackCounter, boolean isParentOperator,
            int depth
    )
    {
        System.out.println(
                "1 - " + operation + " - " + isParentOperator + " - " + depth);
        if(isParentOperator)
        {
            if(operation.getOpcodeType() == Operation.OPCODE_OPERATOR_)
            {
                System.out.println("2 - " + operation);
                if(stackCounter[0] < depth + 1)
                {
                    stackCounter[0] = depth + 1;
                }
                for(Operation refer : operation.getRefer())
                {
                    traverseOperation(refer, stackCounter, true, depth + 1);
                }
                Operation next = operation.getNext();
                if(next != null)
                {
                    traverseOperation(next, stackCounter, true, depth);
                }
            }
            else
            {
                System.out.println("3 - " + operation);
                for(Operation refer : operation.getRefer())
                {
                    traverseOperation(refer, stackCounter, false, 0);
                }
                Operation next = operation.getNext();
                if(next != null)
                {
                    traverseOperation(next, stackCounter, false, 0);
                }
            }
        }
        else
        {
            if(operation.getOpcodeType() == Operation.OPCODE_OPERATOR_)
            {
                System.out.println("4 - " + operation);
                for(Operation refer : operation.getRefer())
                {
                    traverseOperation(refer, stackCounter, true, 0);
                }
                Operation next = operation.getNext();
                if(next != null)
                {
                    traverseOperation(next, stackCounter, true, 0);
                }
            }
            else
            {

                System.out.println("5 - " + operation);
                if(stackCounter[1] < depth + 1)
                {
                    stackCounter[1] = depth + 1;
                }
                for(Operation refer : operation.getRefer())
                {
                    traverseOperation(refer, stackCounter, false, depth + 1);
                }
                Operation next = operation.getNext();
                if(next != null)
                {
                    traverseOperation(next, stackCounter, false, depth);
                }
            }
        }
    }


}
