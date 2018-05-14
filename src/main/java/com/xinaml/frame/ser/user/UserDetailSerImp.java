package com.xinaml.frame.ser.user;

import com.xinaml.frame.base.service.ServiceImpl;
import com.xinaml.frame.dto.user.UserDetailDTO;
import com.xinaml.frame.entity.user.UserDetail;
import org.springframework.stereotype.Service;
/**
 * 用户详情业务接口实现
 *
 * @Author:	[lgq]
 * @Date: [2018-05-14 18:23:27]
 * @Description: [用户详情]
 * @Version: [0.0.1]
 * @Copy: [com.xinaml.frame]
 */
@Service
public class UserDetailSerImp extends ServiceImpl<UserDetail, UserDetailDTO> implements UserDetailSer {
}
