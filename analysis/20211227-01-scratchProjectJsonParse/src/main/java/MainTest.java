import com.alibaba.fastjson.JSON;
import pojo.ProjectJson;
import pojo.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
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
            json        = new String(fis.readAllBytes());

            projectJson = JSON.parseObject(json, ProjectJson.class);

            Target[] targets = projectJson.getTargets();
            System.out.println(targets.length);
            Map<String, String> m = targets[0].getBroadcasts();
            Iterator<String> i = m.keySet().iterator();
            while(i.hasNext())
            {
                String s = i.next();
                System.out.println(s +"------"+ m.get(s));
            }

            fis.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
}
