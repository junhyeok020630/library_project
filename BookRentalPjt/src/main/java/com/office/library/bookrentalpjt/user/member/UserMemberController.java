package com.office.library.bookrentalpjt.user.member;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user/member")
public class UserMemberController {
	
	@Autowired
	UserMemberService userMemberService;
	
	/*
	 * 회원가입
	 */
//	@RequestMapping(value = "/createAccountForm", method = RequestMethod.GET)
	@GetMapping("/createAccountForm")
	// createAccountForm으로 들어오는 Get 요청 처리
	public String createAccountForm() {
		System.out.println("[UserMemberController] createAccountForm()");
		
		String nextPage = "user/member/create_account_form"; // 회원가입 폼 화면으로 이동
		
		return nextPage;
		
	}
	
	/*
	 * 회원가입 확인
	 */
//	@RequestMapping(value = "/createAccountConfirm", method = RequestMethod.POST)
	@PostMapping("/createAccountConfirm")
	// 회원가입 확인 처리 Post 요청
	public String createAccountConfirm(UserMemberVo userMemberVo) {
		// 폼에서 작성한 사용자 정보와 매개변수의 userMemberVo 객체와 바인딩하여 필드 값을 채워준다.
		System.out.println("[UserMemberController] createAccountConfirm()");
		
		String nextPage = "user/member/create_account_ok"; // 기본 페이지 : 회원가입 성공
		
		int result = userMemberService.createAccountConfirm(userMemberVo);
		// 서비스의 createAccountConfirm() 메서드에 회원가입 폼에서 입력한 사용자 정보를 담은 객체 userMemberVo를 매개변수로 전달한다.
		// createAccountConfirm() : 해당 사용자가 존재하는지 확인하고, 존재하지 않는다면 Dao의 insert문 실행
		// 회원가입 성공 : 1 / 이미 존재하는 계정 : 0 / 회원가입 실패 : -1
		
		if (result <= 0) // 이미 존재하는 계정이거나 회원가입 실패 시
			nextPage = "user/member/create_account_ng"; // 회원가입 실패 페이지로 이동
		
		return nextPage;
		
	}
	
	/*
	 * 로그인
	 */
//	@RequestMapping(value = "/loginForm", method = RequestMethod.GET)
	@GetMapping("/loginForm")
	// user/loginForm으로 오는 GET 요청 처리
	public String loginForm() {
		System.out.println("[UserMemberController] loginForm()");
		
		String nextPage = "user/member/login_form"; // login_form.jsp 화면으로 이동
		
		return nextPage;
		
	}
	
	/*
	 * 로그인 확인
	 */
//	@RequestMapping(value = "/loginConfirm", method = RequestMethod.POST)
	@PostMapping("/loginConfirm")
	// loginConfirm으로 들어오는 Post 요청 처리
	// loginForm에서 작성한 데이터의 확인 처리 메서드
	public String loginConfirm(UserMemberVo userMemberVo, HttpSession session) {
		// 로그인 폼에서 작성한 정보를 담은 userMemberVo(id와 비밀번호)를 매개변수로 받아온다.
		// 로그인 성공 시 해당 정보를 저장할 세션을 다루기 위해 HttpSession 클래스를 주입
		System.out.println("[UserMemberController] loginConfirm()");
		
		String nextPage = "redirect:/user/member/login_ok";
		// 기본 페이지 로그인 성공
		// 새로고침 시 로그아웃된 상태에서 재 Post 요청을 막기위해 Get 요청으로 처리하기 위해 리다이렉트
		
		UserMemberVo loginedUserMemberVo = userMemberService.loginConfirm(userMemberVo);
		// id와 비밀번호를 담은 userMemberVo 객체를 서비스의 loginConfirm() 메서드의 매개변수로 전달
		// loginConfirm : Dao의 selectUser() 메서드에 해당 객체를 전송해 id와 pw가 일치하는 사용자 정보를 받아 컨트롤러에 전송해준다.
		
		if (loginedUserMemberVo == null) {
			// 만약 사용자 정보가 존재하지 않는다면
			nextPage = "redirect:/user/member/login_ng";
			// 로그인 실패 페이지로 리다이렉트
			
		} else { // 사용자 정보가 DB에 존재한다면(서비스의 loginConfirm 메서드로부터 반환받은 객체가 존재한다면)
			session.setAttribute("loginedUserMemberVo", loginedUserMemberVo);
			// 현재 세션에 loginedUserMemberVo라는 이름으로 객체 저장
			session.setMaxInactiveInterval(60 * 30); // 세션 만료 시간을 30분으로 설정
			
		}
		
		return nextPage;
		
	}
	@GetMapping("login_ok")
	public String loginOk() {
		return "user/member/login_ok";
	}
	@GetMapping("login_ng")
	public String loginNg() {
		return "user/member/login_ng";
	}
	
	/*
	 * 계정 수정
	 */
//	@RequestMapping(value = "/modifyAccountForm", method = RequestMethod.GET)
	@GetMapping("/modifyAccountForm")
	// modifyAccountForm으로 오는 Get 요청 처리
	// 계정 수정 폼 화면을 제공하는 메서드
	// nav.jsp의 계정 수정 클릭 시 해당 URL로 요청 전송
	public String modifyAccountForm(HttpSession session) {
		// 세션이 만료되었다면 재로그인 하도록 HttpSession 클래스 주입
		System.out.println("[UserMemberController] modifyAccountForm()");
		
		String nextPage = "user/member/modify_account_form"; // 계정 수정 폼 화면 경로
		
		UserMemberVo loginedUserMemberVo = (UserMemberVo) session.getAttribute("loginedUserMemberVo");
//		// 현재 세션에 저장되어 있는 loginedUserMemberVo 객체를 가져온다.
//		// 현재 세션에 저장되어있는 loginedUserMemberVo객체의 필드 값을 불러와 Form 태그의 Value 속성과 매핑시켜준다.
//		// Form 화면에 기본 값으로 loginedUserMemberVo의 필드 값이 들어가 있다.
//		if (loginedUserMemberVo == null) // 해당 객체가 세션에 존재하지 않는다면
//			nextPage = "redirect:/user/member/loginForm"; // 로그인 폼 재요청
		
		return nextPage; // 계정 수정 폼 제공 or 세션 만료시 로그인 폼 요청
		
	}
	
	/*
	 * 회원 정보 수정 확인
	 */
//	@RequestMapping(value = "/modifyAccountConfirm", method = RequestMethod.POST)
	@PostMapping("/modifyAccountConfirm")
	// modifyAccountConfirm으로 오는 Post 요청 처리
	// modify_account_form에서 modify account 클릭 시 해당 Post 요청 전송
	public String modifyAccountConfirm(UserMemberVo userMemberVo, HttpSession session) {
		// 수정 정보를 담은 사용자 객체 userMemberVo 주입
		// 로그인 세션 관리를 위하여 HttpSesion 클래스 주입
		System.out.println("[UserMemberController] modifyAccountConfirm()");
		
		String nextPage = "redirect:/user/member/modify_account_ok";
		// 기본 페이지 계정 수정 성공 페이지
		// 수정 성공 시 세션에 로그인 객체를 다시 저장하기 때문에 get요청으로 리다이렉트 하여 재 Post 요청 문제를 해결한다.

		int result = userMemberService.modifyAccountConfirm(userMemberVo);
		// 수정 정보를 담은 userMemberVo 객체를 서비스의 modifyAccountConfirm() 메서드에 전송
		// modifyAccountConfirm() : 해당 사용자 객체를 Dao의 updateUserAccount() 메서드로 전달 해주고 결과값(DB에서 영향받은 행의 수)를 컨트롤러에 반환
		
		if (result > 0) { // 계정 수정 성공 시
			UserMemberVo loginedUserMemberVo = userMemberService.getLoginedUserMemberVo(userMemberVo.getU_m_no());
			// 서비스의 getLoginedUserMemberVo에 기존 수정 정보를 담고 있는 객체 userMemberVo의 u_m_no값을 매개변수로 전달
			// getLoginedUserMemberVo : Dao의 selectUser(int u_m_no) 메서드를 사용해 DB의 업데이트 완료된 사용자 정보를 담은 객체를 반환받는다.
			// 반환받은 객체를 loginedUserMemberVo에 저장
			
			session.setAttribute("loginedUserMemberVo", loginedUserMemberVo);
			// 세션에 해당 객체(loginedUserMemberVo)를 저장해둔다.
			session.setMaxInactiveInterval(60 * 30);
			// 세션 만료 시간 30분 설정
			
		} else { // 계정 수정 실패 시
			nextPage = "redirect:/user/member/modify_account_ng"; // 계정 수정 실패 페이지로 리다이렉트(세션 설정 관련 코드가 없어 리다이렉트 해주지 않아도 된다.)
		}
		
		return nextPage;
		
	}

	@GetMapping("/modify_account_ok")
	public String modifyAccountOk() {
		return "user/member/modify_account_ok";
	}
	@GetMapping("modify_account_ng")
	public String modifyAccountNg() {
		return "user/member/modify_account_ng";
	}
	
	/*
	 * 로그아웃 확인
	 */
//	@RequestMapping(value = "/logoutConfirm", method = RequestMethod.GET)
	@GetMapping("/logoutConfirm")
	// nav.jsp에서 로그아웃 클릭 시 해당 URL 요청
	public String logoutConfirm(HttpSession session) {
		// 로그아웃시 현재 웹페이지의 로그인 세션을 만료시키기 위해 HttpSession 클래스 주입
		System.out.println("[UserMemberController] logoutConfirm()");
		
		String nextPage = "redirect:/user"; // 로그아웃 시 home화면으로 리다이렉트
		
//		session.removeAttribute("loginedUserMemberVo");
		session.invalidate(); // 세션 만료
		
		return nextPage;
		
	}
	
	/*
	 * 비밀번호 찾기
	 */
//	@RequestMapping(value = "/findPasswordForm", method = RequestMethod.GET)
	@GetMapping("/findPasswordForm")
	// 비밀번호 찾기 폼 화면을 요청하는 메서드
	public String findPasswordForm() {
		System.out.println("[UserMemberController] findPasswordForm()");
		
		String nextPage = "user/member/find_password_form"; // 비밀번호 찾기 폼 화면 find_password_form.jsp로 이동
		
		return nextPage;
		
	}
	
	/*
	 * 비밀번호 찾기 확인
	 */
//	@RequestMapping(value = "/findPasswordConfirm", method = RequestMethod.POST)
	@PostMapping("/findPasswordConfirm")
	// find_password_form에서 find password 클릭 시 해당 경로로 Post 요청
	// 비밀번호 찾기 확인 처리 메서드
	public String findPasswordConfirm(UserMemberVo userMemberVo) {
		// 폼에서 작성한 id, 이름, 이메일을 담은 객체 userMemberVo를 매개변수로 받아온다.
		System.out.println("[UserMemberController] findPasswordConfirm()");
		
		String nextPage = "user/member/find_password_ok"; // 기본페이지 비밀번호 찾기 성공
		
		int result = userMemberService.findPasswordConfirm(userMemberVo);
		// 서비스의 findPasswordConfirm() 메서드의 매개변수로 userMemberVo 전달
		// findPasswordConfirm() : id와 이름, 이메일을 Dao에 전달, (동일한 사용자 찾기)
		// 새로운 비밀번호를 생성하여 Dao에 전달하고(DB 업데이트) 사용자에게 변경된 비밀번호를 메일로 전송하는 메서드

		if (result <= 0) // 비밀번호 찾기 실패 시
			nextPage = "user/member/find_password_ng"; // 실패 화면으로 이동
		
		return nextPage;
		
	}
	
	
}