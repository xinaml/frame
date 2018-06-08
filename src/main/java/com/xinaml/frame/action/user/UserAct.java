package com.xinaml.frame.action.user;

import com.alibaba.fastjson.JSON;
import com.xinaml.frame.base.atction.BaseAct;
import com.xinaml.frame.base.dto.RT;
import com.xinaml.frame.base.entity.ADD;
import com.xinaml.frame.base.entity.EDIT;
import com.xinaml.frame.common.custom.annotation.Login;
import com.xinaml.frame.common.custom.exception.ActException;
import com.xinaml.frame.common.custom.exception.SerException;
import com.xinaml.frame.common.custom.result.ActResult;
import com.xinaml.frame.common.custom.result.Result;
import com.xinaml.frame.common.redis.JRedis;
import com.xinaml.frame.common.utils.PassWordUtil;
import com.xinaml.frame.dto.user.UserDTO;
import com.xinaml.frame.entity.user.User;
import com.xinaml.frame.ser.user.UserSer;
import com.xinaml.frame.to.user.UserTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@Api(value = "UserAPI", tags = {"用户接口"})
@Controller
@RequestMapping("user")
@Login
public class UserAct extends BaseAct {

    @Autowired
    private UserSer userSer;

    @GetMapping({"/", ""})
    public String user(UserDTO dto) throws ActException {
        return "user/user";
    }

    @ResponseBody
    @GetMapping("maps")
    public String maps(UserDTO dto) throws ActException {
        try {
            Map<String, Object> maps = userSer.findByPage(dto);
            return JSON.toJSONString(maps);
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    @ApiOperation(value = "列表")
    @ResponseBody
    @GetMapping("list")
    public Result list(UserDTO dto) throws ActException {
        try {
            dto.addRT(RT.eq("username", "lgq"));
            return new ActResult(userSer.findByRTS(dto));
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }


    @ApiOperation(value = "添加")
    @ApiImplicitParam(dataTypeClass = UserTO.class)
    @ResponseBody
    @PostMapping("add")
    public Result add(@Validated(ADD.class) UserTO to, BindingResult rs) throws ActException {
        try {
            User user = new User();
            BeanUtils.copyProperties(to, user);
            try {
                user.setPassword(PassWordUtil.genSaltPwd("123456"));
            } catch (Exception e) {
                throw new ActException(e.getMessage());
            }
            user.setCreateDate(LocalDateTime.now());
            userSer.save(user);
            return new ActResult("success");
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    // DeleteMapping 这种方式接收不到数组参数，见下一方法，必须用过url来传参
    @ResponseBody
    @PostMapping("del")
    public Result del(String[] ids) throws ActException {
        try {
            userSer.remove(ids);
            return new ActResult("success");
        } catch (Exception e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * Restful 规范
     *
     * @param id
     * @return
     * @throws ActException
     */
    @ResponseBody
    @DeleteMapping("del/{id}")
    public Result del(@PathVariable String id) throws ActException {
        try {
            userSer.remove(id);
            return new ActResult("success");
        } catch (Exception e) {
            throw new ActException(e.getMessage());
        }
    }


    @ResponseBody
    @PutMapping("edit")
    public Result edit(@Validated(EDIT.class) UserTO to, BindingResult rs) throws ActException {
        try {
            to.setExpired(true);
            User user = userSer.findById(to.getId());
            BeanUtils.copyProperties(to, user);
            userSer.update(user);
            return new ActResult("success");
        } catch (Exception e) {
            throw new ActException(e.getMessage());
        }
    }

}
