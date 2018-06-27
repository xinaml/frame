package com.xinaml.frame.ser.user;

import com.alibaba.fastjson.JSON;
import com.xinaml.frame.base.dto.RT;
import com.xinaml.frame.base.service.ServiceImpl;
import com.xinaml.frame.common.custom.exception.SerException;
import com.xinaml.frame.base.rep.RedisRep;
import com.xinaml.frame.common.utils.PassWordUtil;
import com.xinaml.frame.common.utils.TokenUtil;
import com.xinaml.frame.dto.user.UserDTO;
import com.xinaml.frame.entity.user.User;
import com.xinaml.frame.rep.user.UserRep;
import com.xinaml.frame.to.user.LoginTO;
import com.xinaml.frame.to.user.RegisterTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserSerImp extends ServiceImpl<User, UserDTO> implements UserSer {
    private static Logger LOGGER = LoggerFactory.getLogger(UserSerImp.class);
    @Autowired
    private RedisRep jRedis;
    @Autowired
    private UserRep userRep;

    @Override
    public String login(LoginTO to) throws SerException {
        UserDTO dto = new UserDTO();
        dto.addRT(RT.eq("username", to.getUsername())); //username 必须唯一
        User user = findOne(dto);
        String token = null;
        boolean isPass = false;
        if (null != user) {
            try {
                isPass = PassWordUtil.validPwd(to.getPassword(), user.getPassword());
            } catch (Exception e) {
                LOGGER.error("密码解析错误");
            }
        } else {
            throw new SerException("账号或密码错误");
        }
        if (isPass) {
            token = TokenUtil.create(to.getIp());
            jRedis.put(token, JSON.toJSONString(user));
            return token;
        } else {
            throw new SerException("账号或密码错误");
        }
    }

    @Override
    public String register(RegisterTO to) throws SerException {
        if (to.getPassword().equals(to.getRePassword())) {
            if(null==userRep.findByUsername(to.getUsername())){
                try {
                    User user = new User();
                    user.setUsername(to.getUsername());
                    user.setPassword(PassWordUtil.genSaltPwd(to.getUsername()));
                    user.setCreateDate(LocalDateTime.now());
                    super.save(user);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new SerException(e.getMessage());
                }
            }else {
                throw  new SerException("用户名已被占用");
            }

        } else {
            throw new SerException("密码不一致");
        }

        return null;
    }

    @Override
    public Boolean logout(String token) throws SerException {
        if (StringUtils.isNotBlank(token)) {
            jRedis.del(token);
        }
        return true;
    }

    /**
     * 以方法名+查询条件做缓存（例子）
     *
     * @param dto
     * @return
     * @throws SerException
     */
    @Cacheable(value = "users", key = "#root.methodName.concat(#dto.serId)")
    @Override
    public Map<String, Object> findByPage(UserDTO dto) throws SerException {
        return super.findByPage(dto);
    }

    @CacheEvict(value = "users", beforeInvocation = true, allEntries = true)
    @Override
    public void save(User... entities) throws SerException {
        super.save(entities);
    }

    @CacheEvict(value = "users", beforeInvocation = true, allEntries = true)
    @Override
    public void remove(String... ids) throws SerException {
        super.remove(ids);
    }

    @CacheEvict(value = "users", beforeInvocation = true, allEntries = true)
    @Override
    public void update(User... entities) throws SerException {
        super.update(entities);
    }


}
