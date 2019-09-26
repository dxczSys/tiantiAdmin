package com.jeff.tianti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 接口工程欢迎页
 * @author Jeff Xu
 * @since 2017-12-02
 */
@Controller
public class IndexQtController {
	
	/**
	 * 入口
	 * @return
	 */
	@RequestMapping("/index1")
	public String login(){
		return "index1";
	}

}
