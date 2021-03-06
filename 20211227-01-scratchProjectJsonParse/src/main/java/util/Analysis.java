package util;

import com.alibaba.fastjson.JSON;
import pojo.AnalysisResults;
import pojo.Operation;
import pojo.json.*;
import pojo.json.target.*;

import javax.print.DocFlavor;
import java.util.*;
import org.apache.commons.lang3.RandomStringUtils;

public class Analysis
{
    // 得分 1 2 3 和 与之对应的 (opcodetype, 类型)
    public static Map<Integer, String[]> score1 = new HashMap<Integer, String[]>() {{
        put(0, new String[]{"Flow control"});
        put(1, new String[]{"User interactivity", "Flow control"});
        put(18, new String[]{"Synchronization"});
        put(12, new String[]{"Logic"});
        put(33, new String[]{"Data representation"});
    }};
    public static Map<Integer, String[]> score2 = new HashMap<Integer, String[]>() {{
        put(10, new String[]{"Flow control"});
        put(11, new String[]{"Flow control"});
        put(30, new String[]{"Data representation"});
        put(32, new String[]{"Abstraction"});
        put(2, new String[]{"User interactivity"});
        put(3, new String[]{"User interactivity"});
        put(4, new String[]{"User interactivity"});
        put(19, new String[]{"User interactivity"});
        put(20, new String[]{"User interactivity"});
        put(21, new String[]{"User interactivity"});
        put(22, new String[]{"User interactivity"});
        put(23, new String[]{"User interactivity"});
        put(24, new String[]{"User interactivity"});
        put(25, new String[]{"User interactivity"});
        put(26, new String[]{"User interactivity"});
        put(27, new String[]{"User interactivity"});
        put(28, new String[]{"User interactivity"});
        put(6, new String[]{"Synchronization"});
        put(7, new String[]{"Synchronization"});
        put(8, new String[]{"Synchronization"});
        put(13, new String[]{"Logic"});
    }};
    public static Map<Integer, String[]> score3 = new HashMap<Integer, String[]>() {{
        put(14, new String[]{"Flow control", "Synchronization"});
        put(15, new String[]{"Flow control"});
        put(31, new String[]{"Data representation"});
        put(17, new String[]{"Abstraction"});
    }};

    public Analysis()
    {
    }

    public static AnalysisResults analyse(String source)
    {
        AnalysisResults analysisResults = new AnalysisResults();
        // 生成 16 位哈希值
        analysisResults.setSourceHash(RandomStringUtils.random(16, true, true));

        // 解析json
        ProjectJson projectJson = JSON.parseObject(source, ProjectJson.class);
        // 转换为 operation
        Operation[][] totalOperations = rebuildTargets(
                projectJson.getTargets());
        // 统计每种 operation 的数量
        int[] callCounter = new int[Operation.OPCODE_NUMBER];
        for(Operation[] operations : totalOperations)
        {
            int[] stackCounter = new int[2];
            // 判断嵌套逻辑表达式层数
            traverseOperations(operations, stackCounter);
            if (stackCounter[0] >= 3) {
                analysisResults.changeScore("Logic", 3);
            } else if (stackCounter[0] == 2) {
                analysisResults.changeScore("Logic", 2);
            } else if (stackCounter[0] == 1) {
                analysisResults.changeScore("Logic", 1);
            }
            for(Operation operation : operations)
            {
                callCounter[operation.getOpcodeType()]++;
            }

        }

        // TODO 需要在此实现具体的分析函数。
        for (int i = 0; i < Operation.OPCODE_NUMBER; ++i) {
            // 如果有此种opcode
            if (callCounter[i] != 0) {
                if (score3.containsKey(i)) {
                    String[] temp = score3.get(i);
                    for (String opcode : temp) {
                        analysisResults.changeScore(opcode, 3);
                    }
                } else if(score2.containsKey(i)) {
                    String[] temp = score2.get(i);
                    for (String opcode : temp) {
                        analysisResults.changeScore(opcode, 2);
                    }
                } else if(score1.containsKey(i)) {
                    String[] temp = score1.get(i);
                    for (String opcode : temp) {
                        analysisResults.changeScore(opcode, 1);
                    }
                }
            }
        }
        return analysisResults;
    }

    // 将解析 json 得到的 targets 转换为 Operation 数组
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
        // "block id" : " {"opcode":, xxx }"
        Map<String, Block> blocks = target.getBlocks();

        LinkedList<String[]> parentRelationships = new LinkedList<>();
        LinkedList<String[]> nextRelationships = new LinkedList<>();
        LinkedList<String[]> referRelationships = new LinkedList<>();

        for(String id : blocks.keySet())
        {
            Block block = blocks.get(id);
            // 记录 block 的 "next" "parent"信息
            if(block.getParent() != null)
            {
                parentRelationships.add(new String[]{id, block.getParent()});
            }
            if(block.getNext() != null)
            {
                nextRelationships.add(new String[]{id, block.getNext()});
            }

            // inputs 跳转
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

    // 判断opcode类型
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
        else if(opcode.contains("data_") && opcode.contains("varible"))
        {
            return Operation.OPCODE_DATA_VARIABLE;
        }
        else if(opcode.contains("data") && opcode.contains("list")) {
            return Operation.OPCODE_DATA_LIST;
        }
        else if(opcode.contains("procedures_"))
        {
            return Operation.OPCODE_PROCEDURES_;
        }
        else if(opcode.contains("motion_")) {
            return Operation.OPCODE_MOTION_;
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
