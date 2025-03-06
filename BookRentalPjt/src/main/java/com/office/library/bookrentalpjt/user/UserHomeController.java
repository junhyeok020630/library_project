package com.office.library.bookrentalpjt.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
// 사용자 홈 컨트롤러
// user로 오는 요청 처리
// nav.jsp - User Home 클릭 시 url에 user로 요청 전송
public class UserHomeController {

//	@RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
	@GetMapping({"", "/"})
	// /user로 들어오는 기본 Get 요청
	public String home() {
		System.out.println("[UserHomeController] home()");
		
		String nextPage = "user/home"; // user/home.jsp 화면으로 이동
		
		return nextPage;
		
	}
	
}
