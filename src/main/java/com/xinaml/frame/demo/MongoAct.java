package com.xinaml.frame.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xinaml.frame.base.atction.BaseAct;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * mongo测试（目的为存储动态字段的表结构数据）
 */
@Controller
@RequestMapping("mongo")
public class MongoAct extends BaseAct {
    @Autowired
    private TableRep tableRep;


    @ResponseBody
    @GetMapping("table/save")
    public ActResult saveTable() {
        Table table = new Table();
        table.setId(UUID.randomUUID().toString());
        table.setName("test22");
        table.setCreateTime(LocalDateTime.now().minusDays(1));
        FieldConf username = new FieldConf("username", FieldType.STRING);
        FieldConf password = new FieldConf("password", FieldType.STRING);
        FieldConf age = new FieldConf("age", FieldType.STRING);
        table.setFields(Arrays.asList(username, password, age));
        tableRep.save(table);
        return new ActResult((Object) table.getId());
    }

    @ResponseBody
    @GetMapping("table/list")
    public ActResult findTable() {
        List<Table> tables = tableRep.find(null);
        return new ActResult(tables);
    }

    @ResponseBody
    @GetMapping("table/field/{tableId}")
    public ActResult findFieldByTable(@PathVariable String tableId) {
        Table table = tableRep.findById(tableId);
        return new ActResult(table.getFields());
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @ResponseBody
    @GetMapping("data/save")
    public ActResult saveData(@RequestParam String tableId) throws ActException {
        try {
            Table table = tableRep.findById(tableId);
            List<Field> fields = new ArrayList<>();
            for (FieldConf conf : table.getFields()) {
                Field field = new Field(conf.getName(), "666", conf.getType());
                if (!field.getName().equals("age")) {
                    fields.add(field);
                }
            }
            Object o = BeanUtil.createObj(fields);
            mongoTemplate.save(JSON.toJSON(o), "dynamic_" + table.getName());
            return new ActResult(SUCCESS);
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("data/list/{tableId}")
    public ActResult list(@PathVariable String tableId) throws ActException {
        Table table = tableRep.findById(tableId);
        Query query = new Query(Criteria.where("username").is("123").and("password").is("123"));
//        Query query = new Query();
        List<JSONObject> objects = mongoTemplate.find(query, JSONObject.class, "dynamic_" + table.getName());
        for (Iterator<JSONObject> it = objects.iterator(); it.hasNext(); ) {
            JSONObject obj = it.next();
            obj.remove("_id");
        }
        return new ActResult(objects);
    }

}
