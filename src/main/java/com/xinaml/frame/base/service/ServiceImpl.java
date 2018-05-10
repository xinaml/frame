/**
 * 基础业务
 *
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.frame.base.service;

import com.xinaml.frame.base.dto.BaseDTO;
import com.xinaml.frame.base.entity.BaseEntity;
import com.xinaml.frame.base.rep.JapRep;
import com.xinaml.frame.base.rep.JpaSpec;
import com.xinaml.frame.common.custom.exception.RepException;
import com.xinaml.frame.common.custom.exception.SerException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Table;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ServiceImpl<BE extends BaseEntity, BD extends BaseDTO> implements Ser<BE, BD>, Serializable {
    @Autowired(required = false)
    protected JapRep<BE, BD> rep;
    @Autowired
    protected EntityManager entityManager;

    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public List<BE> findAll() throws SerException {
        try {
            return rep.findAll();
        } catch (Exception e) {
            throw new SerException(e.getMessage());
        }

    }

    @Override
    public  Map<String,Object> findByPage(BD dto) throws SerException {
        try {
            JpaSpec jpaSpec = new JpaSpec<BE, BD>(dto);
            PageRequest page = jpaSpec.getPageRequest(dto);
            Page<BE> rs = rep.findAll(jpaSpec, page);
            Map map = new HashMap<BE, Long>(2);
            map.put("rows", rs.getContent());
            map.put("total", rs.getTotalElements());
            return map;
        } catch (Exception e) {
            throw new SerException(e.getMessage());
        }

    }

    @Override
    public Long count(BD dto) throws SerException {
        try {
            JpaSpec jpaSpec = new JpaSpec<BE, BD>(dto);
            return rep.count(jpaSpec);
        } catch (Exception e) {
            throw new SerException(e.getMessage());
        }

    }

    @Override
    public BE findOne(BD dto) throws SerException {
        JpaSpec jpaSpec = new JpaSpec<BE, BD>(dto);
        PageRequest page = jpaSpec.getPageRequest(dto);
        List<BE> list = rep.findAll(jpaSpec, page).getContent();
        if (null != list && list.size() > 1) {
            throw new SerException("find two and more data!");
        }
        return null != list && list.size() > 0 ? list.get(0) : null;
    }


    @Override
    public List<BE> findByRTS(BD dto) throws SerException {

        try {
            JpaSpec jpaSpec = new JpaSpec<BE, BD>(dto);
            return rep.findAll(jpaSpec);
        } catch (Exception e) {
            throw new SerException(e.getMessage());
        }
    }

    @Override
    public BE findById(String id) throws SerException {
        try {
            return rep.findById(id).get();
        } catch (Exception e) {
            throw new SerException(e.getMessage());
        }
    }

    @Override
    public void save(BE... entities) throws SerException {
        try {
            rep.saveAll(Arrays.asList(entities));
        } catch (Exception e) {
            throw new SerException(e.getMessage());
        }
    }

    @Override
    public void remove(String... ids) throws SerException {

        if (null != ids) {
            try {
                for (String id : ids) {
                    rep.deleteById(id);
                }
            } catch (Exception e) {
                throw new SerException(e.getMessage());
            }
        } else {
            throw new SerException("id 不能为空");
        }


    }

    @Override
    public void remove(BE... entities) throws SerException {
        try {
            rep.deleteAll(Arrays.asList(entities));
        } catch (Exception e) {
            throw new SerException(e.getMessage());
        }
    }

    @Override
    public void update(BE... entities) throws SerException {
        try {
            for (BE be : entities) {
                rep.save(be);
            }
        } catch (Exception e) {
            throw new SerException(e.getMessage());
        }
    }

    @Override
    public Boolean exists(String id) throws SerException {
        try {
            return rep.existsById(id);
        } catch (Exception e) {
            throw new SerException(e.getMessage());
        }
    }

    @Override
    public List<Object> findBySql(String sql) throws SerException {
        try {
            Query nativeQuery = entityManager.createNativeQuery(sql);
            return nativeQuery.getResultList();
        } catch (Exception e) {
            throw new SerException(e.getMessage());
        }

    }

    @Override
    public <T> List<T> findBySql(String sql, Class clazz, String[] fields) throws SerException {
        List<Field> all_fields = new ArrayList<>(); //源类属性列表
        Class temp_clazz = clazz;
        while (null != temp_clazz) { //数据源类所有属性（包括父类）
            all_fields.addAll(Arrays.asList(temp_clazz.getDeclaredFields()));
            temp_clazz = temp_clazz.getSuperclass();
            if (Object.class.equals(temp_clazz) || null == temp_clazz) {
                break;
            }
        }
        Query nativeQuery = entityManager.createNativeQuery(sql);
        List<Object> resultList = nativeQuery.getResultList();
        List<T> list = new ArrayList<>(resultList.size());

        //解析查询结果
        try {
            for (int i = 0; i < resultList.size(); i++) {
                Object[] arr_obj;
                if (fields.length > 1) {
                    arr_obj = (Object[]) resultList.get(i);
                } else {
                    arr_obj = new Object[]{resultList.get(i)};
                }
                Object obj = clazz.newInstance();
                for (int j = 0; j < fields.length; j++) {
                    for (Field field : all_fields) {
                        if (field.getName().equals(fields[j])) {
                            field.setAccessible(true);
                            if (!field.getType().isEnum()) { //忽略枚举类型
                                field.set(obj, convertDataType(field.getType().getSimpleName(), arr_obj[j]));
                            } else {
                                Field[] enumFields = field.getType().getFields();
                                for (int k = 0; k < enumFields.length; k++) {
                                    Integer val = null;
                                    if (null != arr_obj[j]) {
                                        val = Integer.parseInt(arr_obj[j].toString());
                                    }
                                    if (null != val && val == k) {
                                        String name = enumFields[k].getName();
                                        field.set(obj, field.getType().getField(name).get(name));
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
                list.add((T) obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw repExceptionHandler(new RepException(e.getMessage()));
        }

        return list;
    }

    @Override
    public void executeSql(String sql) throws SerException {
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
    }

    @Override
    public String getTableName(Class clazz) throws SerException {
        try {
            if (clazz.isAnnotationPresent(Table.class)) {
                Annotation annotation = clazz.getAnnotation(Table.class);
                Method[] methods = annotation.annotationType().getMethods();
                for (Method method : methods) {
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    if ("name".equals(method.getName())) {
                        Object invoke = method.invoke(annotation);
                        return invoke.toString();
                    }
                }
            }
        } catch (Exception e) {
            throw new SerException(e.getMessage());
        }

        throw new SerException("解析表名错误!");
    }

    /**
     * 数据库类型转换
     *
     * @param obj
     * @return
     */
    private Object convertDataType(String type, Object obj) {
        if (null != obj) {
            String val = obj.toString();
            switch (type) {
                case "int":
                    obj = Integer.parseInt(val);
                    break;
                case "Float":
                    obj = Float.parseFloat(val);
                    break;
                case "Double":
                    obj = Double.parseDouble(val);
                    break;
                case "Long":
                    obj = Long.parseLong(val);
                    break;
                case "BigDecimal":
                    obj = Double.parseDouble(val);
                    break;
                case "Boolean":
                    obj = Boolean.parseBoolean(val);
                    break;
                case "Integer":
                    obj = Integer.parseInt(val);
                    break;
                case "LocalDateTime":
                    obj = LocalDateTime.parse(StringUtils.substring(val, 0, val.length() - 2), DATE_TIME);
                    break;
                case "LocalTime":
                    obj = LocalDateTime.parse(val, TIME);
                    break;
                case "LocalDate":
                    obj = LocalDate.parse(val, DATE);
                    break;
                default:
                    obj = String.valueOf(obj);
                    break;

            }
        }
        return obj;
    }

    private SerException repExceptionHandler(RepException e) {
        String msg = "";
        switch (e.getType()) {
            case NOT_FIND_FIELD:
                msg = "非法查询";
                break;
            case ERROR_ARGUMENTS:
                msg = "参数不匹配";
                break;
            case ERROR_PARSE_DATE:
                msg = "时间类型转换错误,字段类型不匹配";
                break;
            case ERROR_NUMBER_FORMAT:
                msg = "整形转换错误,字段类型不匹配";
                break;
            default:
                msg = e.getMessage();
        }
        return new SerException(msg);
    }

}
