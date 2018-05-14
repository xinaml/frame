package beetl.utils;

import beetl.entity.ClazzField;
import beetl.entity.ClazzInfo;
import com.xinaml.frame.common.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.FileResourceLoader;
import org.springframework.data.domain.Page;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 解析bean 信息
 */
public class ParseBean {
    public static String rootPath = System.getProperty("user.dir") + "/src/main/java/"; //模板目录
    public static String tmpPath = System.getProperty("user.dir") + "/src/test/java/beetl/Bean"; //模板目录
    public static String jtmPath = System.getProperty("user.dir") + "/src/test/java/beetl/template";//模板文件目录

    public static ClazzInfo clazzInfo; //类信息
    public static List<ClazzField> fields; //字段信息
    public static String[] info = new String[]{"dir", "packages", "className", "author", "des", "version"};//类信息
    public static String[] types = new String[]{"Integer", "String", "Double", "Float", "Byte", "Character", "Boolean"};//类信息

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
        rootPath = rootPath + clazzInfo.getDir().replaceAll("\\.", "/") + "/";
        createFile(rootPath, clazzInfo.getPackages()); //创建目录及文件
    }

    /**
     * 创建文件
     *
     * @param path
     * @param packages
     */
    private static void createFile(String path, String packages) {
        try {
            // 创建entity
            createModuleAndClazz(path, "entity", packages);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
    }

    private static void createModuleAndClazz(String path, String module, String packages) throws IOException {
        File moduleDir = new File(path + "/" + module); //模块目录 dao dto entity vo service 等。。。
        if (!moduleDir.exists()) {
            moduleDir.mkdirs();
        }
        File packagesDir = new File(moduleDir.getPath() + "/" + packages);//包目录 user 等业务模块等。。。
        if (!packagesDir.exists()) {
            packagesDir.mkdirs();
        }
        //载入模板数据
        FileResourceLoader resourceLoader = new FileResourceLoader(jtmPath, "utf-8");
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        Template template = gt.getTemplate(module + ".jtm");
        template.binding("package", clazzInfo.getPackages());
        template.binding("className", clazzInfo.getClassName());
        template.binding("tableName", clazzInfo.getTableName());
        template.binding("author", clazzInfo.getAuthor());
        template.binding("des", clazzInfo.getDes());
        template.binding("dir", clazzInfo.getDir());
        template.binding("version", clazzInfo.getVersion());
        List<String> importPackage = initImportPackage();
        template.binding("importPackage", importPackage);
        template.binding("list", fields);
        //写入文件
        File file = new File(packagesDir + "/" + clazzInfo.getClassName() + ".java");
        FileWriter fw = new FileWriter(file);
        fw.write(template.render());
        fw.close();
    }

    /**
     * 其他java类型导入包
     *
     * @return
     */
    private static List<String> initImportPackage() {
        List<String> importPackage = new ArrayList<>();
        for (ClazzField field : fields) {
            boolean exist = false;
            String fieldType = field.getType().trim();
            for (String type : types) {
                if (type.equals(fieldType)) {
                    exist = true;
                }
            }
            if (!exist) {
                switch (fieldType) {
                    case "LocalDate":
                        importPackage.add("import java.time.LocalDate;");
                        break;
                    case "LocalDateTime":
                        importPackage.add("import java.time.LocalDateTime;");
                        break;
                    case "LocalTime":
                        importPackage.add("import java.time.LocalTime;");
                        break;
                    default: { //自定义类型
                        String dir = rootPath + "entity";
                        String filePath = searchFile(new File(dir).listFiles(), fieldType);
                        if (StringUtils.isNotBlank(filePath)) {
                            filePath =StringUtils.substringAfter(filePath,"main/java");
                            filePath = filePath.replaceAll("/", "\\.");
                            filePath = filePath.replaceAll("\\\\", "\\.");
                            filePath =filePath.substring(1,filePath.length()-5);
                            importPackage.add("import "+filePath+";");
                            //添加注解
                            String annotation="@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)\n" +
                                    "    @JoinColumn(name = \""+ StringUtil.toLowerFirst(fieldType)+"_id\", columnDefinition = \"VARCHAR(36) COMMENT '"+field.getDes()+"' \")";
                            field.setAnnotation(annotation);
                        }

                    }
                }
            }
            if(null==field.getAnnotation()){ //添加注解
                field.setAnnotation("@Column(columnDefinition = \"VARCHAR(50) COMMENT '"+field.getDes()+"' \")");
            }
        }
        return importPackage;
    }


    private static String searchFile(File[] files, String fieldType) {
        String path = null;
        while (files != null && files.length > 0) {
            for (File f : files) {
                if (fieldType.equals(f.getName().split("\\.")[0])) {
                    path = f.getPath();
                    return path;
                } else {
                    if (f.isDirectory()) {
                        path = searchFile(f.listFiles(), fieldType);
                        if(null!=path){
                            return path;
                        }
                    } else if (f.isFile() && files.length == 1) {
                        files = null;
                    }
                }
            }

        }
        return path;
    }
}
