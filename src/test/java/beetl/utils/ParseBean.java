package beetl.utils;

import beetl.entity.ClazzField;
import beetl.entity.ClazzInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 解析bean 信息
 */
public class ParseBean {
    public static String tmpPath = System.getProperty("user.dir") + "/src/test/java/beetl/Bean"; //模板目录
    public static ClazzInfo clazzInfo; //类信息
    public static List<ClazzField> fields; //字段信息
    public static String[] info = new String[]{"dir", "packages", "className", "author", "des", "version"};//类信息

    static {
        try {
            File f = new File(tmpPath);
            BufferedReader reader = new BufferedReader(new FileReader(f));
            List<String> lines = reader.lines().collect(Collectors.toList());
            int index = 0;
            boolean isInfo = true;
            clazzInfo = new ClazzInfo();
            fields = new ArrayList<>();
            for (String line : lines) {
                if (line.startsWith("#")) {
                    isInfo = false;
                    continue;
                }
                if (line.trim().length() > 2 && isInfo) {
                    Field field = clazzInfo.getClass().getDeclaredField(info[index++]);
                    field.setAccessible(true);
                    field.set(clazzInfo, line.split(":")[1].trim());
                }
                if (line.trim().length() > 2 && !isInfo) {
                    String[] field = line.split(" ");
                    ClazzField clazzField = new ClazzField(field[0], field[1], field[2]);
                    fields.add(clazzField);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String path = System.getProperty("user.dir") + "/src/main/java/" + clazzInfo.getDir().replaceAll("\\.","/") + "/" + clazzInfo.getPackages();
        createFile(path); //创建目录
    }

    private static void createFile(String path) {
        System.out.println(path);
    }
}
