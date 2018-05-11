package beetl;

import beetl.entity.ClazzField;
import beetl.utils.ParseBean;
import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileWriter;

public class Build {


    public static void main(String[] args) throws Exception {

        System.out.println(JSON.toJSONString(ParseBean.clazzInfo));
        for(ClazzField field:ParseBean.fields){
            System.out.println(JSON.toJSON(field));
        }

    }


    private static FileWriter wt = null;
    private static File folder = null;
    private static File file = null;
    /**
     * 创建文件或者文件夹
     *
     * @param folderPath
     * @param filePath
     * @param content
     * @throws Exception
     */
    private static void createFile(String folderPath, String filePath, String content) throws Exception {
        folder = new File(folderPath);

        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }
        file = new File(filePath);
        if (folder.exists() && !file.exists()) {
            file.createNewFile();
        }
        wt = new FileWriter(file);
        wt.write(content);
        wt.close();
    }

}
