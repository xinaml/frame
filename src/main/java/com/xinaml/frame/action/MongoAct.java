package com.xinaml.frame.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xinaml.frame.base.atction.BaseAct;
import com.xinaml.frame.base.rep.MongoRep;
import com.xinaml.frame.common.custom.exception.ActException;
import com.xinaml.frame.common.custom.exception.SerException;
import com.xinaml.frame.common.custom.result.ActResult;
import com.xinaml.frame.common.utils.BeanUtil;
import com.xinaml.frame.entity.dynamic.Field;
import com.xinaml.frame.entity.dynamic.FieldConf;
import com.xinaml.frame.entity.dynamic.Table;
import com.xinaml.frame.rep.dynamic.TableRep;
import com.xinaml.frame.types.FieldType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("mongo")
public class MongoAct extends BaseAct {
    @Autowired
    private TableRep tableRep;


    @ResponseBody
    @GetMapping("table/save")
    public ActResult saveTable() {
        Table table = new Table();
        table.setId("888");
        table.setName("test");
        table.setCreateTime(LocalDateTime.now().minusDays(1));
        FieldConf username = new FieldConf("username", FieldType.STRING);
        FieldConf password = new FieldConf("password", FieldType.STRING);
        table.setFields(Arrays.asList(username, password));
        tableRep.save(table);
        return new ActResult(SUCCESS);
    }

    @ResponseBody
    @GetMapping("table/list")
    public ActResult findTable() {
        List<Table> tables = tableRep.find(null);
        return new ActResult(tables);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @ResponseBody
    @GetMapping("data/save")
    public ActResult saveData() throws ActException {
        try {
            Table table = tableRep.findById("1");
            List<Field> fields = new ArrayList<>();
            for (FieldConf conf : table.getFields()) {
                Field field = new Field(conf.getName(), "234", conf.getType());
                fields.add(field);
            }
            Object o = BeanUtil.createObj(fields);
            mongoTemplate.save(JSON.toJSON(o), "rows");
            return new ActResult(SUCCESS);
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("data/list")
    public ActResult list() throws ActException {
        Query query =new Query(Criteria.where("username").is("123").and("password").is("123"));
        List<JSONObject> objects = mongoTemplate.find(query,JSONObject.class,"rows");
        return new ActResult(objects);

    }

}
