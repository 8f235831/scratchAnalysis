import com.alibaba.fastjson.JSON;
import pojo.AnalysisResults;
import pojo.Operation;
import pojo.json.ProjectJson;
import util.Analysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;

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
            fis.close();
            projectJson = JSON.parseObject(json, ProjectJson.class);

            Operation[][] totalOperations = Analysis
                    .rebuildTargets(projectJson.getTargets());

            int[] stackCounter = new int[2];
            for(Operation[] list : totalOperations)
            {
                Analysis.traverseOperations(list, stackCounter);
            }
            System.out.println(Arrays.toString(stackCounter));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
