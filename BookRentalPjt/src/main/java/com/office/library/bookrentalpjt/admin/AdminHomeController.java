package com.office.library.bookrentalpjt.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {

	@RequestMapping(value = {"", "/"}, method = RequestMethod.GET) // 관리자 home 페이지 요청
	public String home() {
		System.out.println("[AdminHomeController] home()"); // 콘솔에 로그 출력
		
		String nextPage = "admin/home"; // admin의 home.jsp 반환
		
		return nextPage;
		
	}

}