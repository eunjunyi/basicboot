package com.mor.test.sess.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mor.test.sess.security.service.SecurityService;
import com.mor.test.session.vo.SessionDto;

@Controller
public class LoginController {
	
	@Autowired
	SecurityService securityService;
	
	//회원가입 페이지 이동
	@GetMapping("/join/join")
	public String join() {
		return  "login/join";
	}
	
	//로그인 페이지 이동
	@RequestMapping("/login/login")
	public ModelAndView lgoin(@RequestParam(value="msg", required=false) String msg) {
		System.out.println("로그인 페이지호출");
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("msg",msg);
		mv.setViewName("/login/login");
		return mv;
	}
	
	//로그인 에러페이지 이동
	@GetMapping("/login/login-error")
	public String error() {
		return  "login/error";
	}
	
	//아이디 중복체크
    @PostMapping("/join/idCheck")
    @ResponseBody
    public String idCheck(@RequestParam String inputId) throws Exception{
    	SessionDto member = securityService.getSelectMeberInfo(inputId);
        String canUse = member != null ? "" : "Y";
		return canUse;
    }
    
    //회원가입 Insert 
    @PostMapping("/join/insert")
    public String setInsertMember(SessionDto member) throws Exception{
        if(securityService.setInsertMember(member) > 0){
    		return  "login/login";	
        }else {
    		return  "join/join";
        }
    }
    
    @RequestMapping("/home")
	public String home() {
		return  "home/home";
	}
	
	@RequestMapping("/user/main")
	public String user() {
		return  "user/main";
	}
	
	@RequestMapping("/admin/main")
	public String admin() {
		return  "admin/main";
	}
	
}
	
