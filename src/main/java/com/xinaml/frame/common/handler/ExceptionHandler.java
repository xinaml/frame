package com.xinaml.frame.common.handler;

import com.xinaml.frame.common.exception.ActException;
import com.xinaml.frame.common.http.ResponseContext;
import com.xinaml.frame.common.result.ActResult;
import com.xinaml.frame.common.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常统一处理
 *
 * @author lgq
 * @date 2018/4/15
 */
@Component
public class ExceptionHandler extends AbstractHandlerExceptionResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);
    private static final String JSON_CONTEXT = "text/html;charset=utf-8";
    private static final int SUCCESS_STATUS = 200;
    private static final int EXCEPTION_STATUS = 500;
    private static final int EXCEPTION_CODE = -1;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        e.printStackTrace();
        ActResult rs = new ActResult();
        httpServletResponse.setContentType(JSON_CONTEXT);
        if (e instanceof ActException) {
            String code = StringUtils.substringBefore(e.getMessage(), "@");
            if (StringUtils.isNumeric(code)) {
                rs.setCode(Integer.parseInt(code));
                rs.setMsg(StringUtils.substringAfter(e.getMessage(), "@"));
            } else {
                rs.setCode(1);
            }
            httpServletResponse.setStatus(SUCCESS_STATUS);
        } else {
            httpServletResponse.setStatus(EXCEPTION_STATUS);
            rs.setCode(EXCEPTION_CODE);
        }
        if (!StringUtil.isChinese(e.getMessage())) {
            LOGGER.error(o + e.getMessage());
            rs.setMsg("服务器错误");
        } else {
            if (StringUtils.isBlank(rs.getMsg())) {
                rs.setMsg(e.getMessage());
                LOGGER.info(o + rs.getMsg());
            }
        }

        /**
         * 处理数据库异常
         */
        String msg = handleJapException(e);
        if (StringUtils.isNotBlank(msg)) {
            rs.setMsg(msg);
        }
        ResponseContext.writeData(rs);
        return new ModelAndView();
    }

    private String handleJapException(Throwable throwable) {
        String msg = throwable.getMessage();
        String result;
        result = StringUtils.substringAfter(msg, "Caused by: java.sql.SQLIntegrityConstraintViolationException:");

        if (StringUtils.isNotBlank(result)) {
            /**
             * 处理唯一约束
             */
            result = StringUtils.substringBefore(result, "' for key");
            result = StringUtils.substringAfter(result, "Duplicate entry '");
            if (StringUtils.isNotBlank(result)) {
                return "[" + result + "]该名称已被占用!";
            }
            /**
             * 处理非空约束
             */

            result = StringUtils.substringBefore(result, "' cannot be null");
            result = StringUtils.substringAfter(result, "Column '");
            if (StringUtils.isNotBlank(result)) {
                return "[" + result + "]不能为空!";
            }
        }
        result = StringUtils.substringAfter(msg, "com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column '");
        if (StringUtils.isNotBlank(result)) {
            /**
             * 数据长度
             */
            result = StringUtils.substringBefore(result, "' at row");
            return "[" + result + "]超出长度范围!";
        }
        return result;
    }


}
