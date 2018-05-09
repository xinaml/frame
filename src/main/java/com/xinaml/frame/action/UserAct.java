package com.xinaml.frame.action;

import com.xinaml.frame.base.atction.BaseAct;
import com.xinaml.frame.base.dto.RT;
import com.xinaml.frame.base.entity.ADD;
import com.xinaml.frame.common.custom.exception.ActException;
import com.xinaml.frame.common.custom.exception.SerException;
import com.xinaml.frame.common.custom.result.ActResult;
import com.xinaml.frame.common.custom.result.Result;
import com.xinaml.frame.dto.UserDTO;
import com.xinaml.frame.entity.User;
import com.xinaml.frame.ser.UserSer;
import com.xinaml.frame.to.UserTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Api(value = "UserAPI", tags = {"用户接口"})
@Controller
@RequestMapping("user")
public class UserAct extends BaseAct {

    @Autowired
    private UserSer userSer;

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
            user.setCreateDate(LocalDateTime.now());
            userSer.save(user);
            return new ActResult("success");
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }


}
