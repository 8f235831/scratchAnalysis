import com.alibaba.fastjson.JSON;
import pojo.Operation;
import pojo.json.ProjectJson;
import pojo.json.Target;
import pojo.json.target.Block;
import util.Analysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public class MainTest
{
    public static void main(String[] args)
    {
        String filePath = "src/main/resources/project.json";
        String json;
        ProjectJson projectJson;
        File file = new File(filePath);
        try
        {
            InputStream fis = new FileInputStream(file);
            json = new String(fis.readAllBytes());

            projectJson = JSON.parseObject(json, ProjectJson.class);

            Target[] targets = projectJson.getTargets();
            Target target = targets[1];
            Operation[] operations = Analysis.rebuildTarget(target);
            for(Operation o : operations)
            {
                System.out.println(o + " - " + o.getOpcodeType());
            }

            fis.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}