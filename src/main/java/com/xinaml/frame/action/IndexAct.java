/**
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.frame.action;

import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页
 *
 * @author lgq
 */
@Controller
public class IndexAct {

    @GetMapping("/")
    public String index(String data) {
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        System.out.println(path);
        System.out.println("utf-8s 测试：" + data);
        return "index";
    }
}
