/**
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.frame.action;

import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 首页
 *
 * @author lgq
 */
@Controller
public class IndexAct {


    @GetMapping("/")
    public ModelAndView index(String data) {
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        System.out.println(path);
        System.out.println("utf-8s 测试,传递参数为：" + data);
        return new ModelAndView("index");
    }
}
