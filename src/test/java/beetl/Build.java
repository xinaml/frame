package beetl;

import beetl.entity.ClazzField;
import beetl.utils.ParseBean;
import com.alibaba.fastjson.JSON;

public class Build {


    public static void main(String[] args) throws Exception {

        System.out.println(JSON.toJSONString(ParseBean.clazzInfo));
        for (ClazzField field : ParseBean.fields) {
            System.out.println(JSON.toJSON(field));
        }

    }


}
