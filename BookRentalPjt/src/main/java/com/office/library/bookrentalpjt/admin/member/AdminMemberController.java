package com.office.library.bookrentalpjt.admin.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller // 해당 클래스를 컨트롤러로 빈등록하는 어노테이션
@RequestMapping("/admin/member") // admin/member 페이지 내의 요청 처리
@RequiredArgsConstructor // lombok에서 제공하는 어노테이션; private final이 붙은 필드를 포함하는 생성자를 만든다. (Autowired를 사용하지 않아도 된다.)
public class AdminMemberController {

	private final AdminMemberService adminMemberService; // adminMemberService의 메서드를 사용하기 위해 주입받는다.
	
	/*
	 * 회원 가입
	 */
//	@RequestMapping(value = "/createAccountForm", method = RequestMethod.GET)
	@GetMapping("/createAccountForm") // createAccountForm 요청 - admin/include/nav.jsp
	public String createAccountForm() {
		System.out.println("[AdminMemberController] createAccountForm()"); // 콘솔 출력
		
		String nextPage = "admin/member/create_account_form"; // admin/member의 create_account_form.jsp 매핑
		
		return nextPage;
		
	}
	
	/*
	 * 회원 가입 확인
	 */
//	@RequestMapping(value = "/createAccountConfirm", method = RequestMethod.POST)
	@PostMapping("/createAccountConfirm") // create_account_form에서 form으로 회원가입 요청을 Post로 받아옴
	public String createAccountConfirm(AdminMemberVo adminMemberVo) { // form 태그의 내용을 AdminMemberVo 객체로 받아옴
		System.out.println("[AdminMemberController] createAccountConfirm()");
		
		String nextPage = "admin/member/create_account_ok"; // 다음페이지를 회원가입 성공 페이지로 매핑한다.
		
		int result = adminMemberService.createAccountConfirm(adminMemberVo);
		// adminMemberService의 createAccountConfirm 메서드를 통해 Vo 객체로 불러온 회원가입 정보가 잘못되거나 이미 존재하는 회원인지 아닌지 확인한다.
		
		if (result <= 0) // 잘못이 있다면 (이미 존재 = 0; 회원 가입 실패 = -1)
			nextPage = "admin/member/create_account_ng"; // 회원가입 실패 페이지로 매핑한다.
		
		return nextPage;
	}
	/*
	 * 로그인
	 */
//	@RequestMapping(value = "/loginForm", method = RequestMethod.GET)
	@GetMapping("/loginForm") // loginForm으로 오는 get 요청을 처리한다.(admin/include/nav.jsp에서 하이퍼링크로 요청 중)
	public String loginForm() {
		System.out.println("[AdminMemberController] loginForm()");

		String nextPage = "admin/member/login_form"; // 로그인 폼 페이지 반환

		return nextPage;

	}

	/*
	 * 로그인 확인
	 */
//	@RequestMapping(value = "/loginConfirm", method = RequestMethod.POST)
	@PostMapping("/loginConfirm") // loginConfirm으로 가는 Post 요청을 처리한다. Post 내용은 login_form.jsp의 form 태그에 담겨있다.
	public String loginConfirm(AdminMemberVo adminMemberVo, HttpSession session) {
		// AdminMemberVo : DB에 저장되어있는 관리자 정보를 담아서 반환해줄 객체이다.
		// HttpSession : session 정보를 저장할 수 있는 클래스로 session 매개변수에 세션 정보를 담아 전송
		/*
		 * 쿠키와 세션 : 웹 서비스에서 로그인 상태를 유지하기 위한 방법
		 * 이를 설정하지 않으면 관리자 로그인에 성공하여도 화면의 메뉴가 변하지 않는다. (관리자 로그인시 관리자 전용 메뉴가 나와야함)
		 * 쿠키 : 접속 정보를 클라이언트에 작은 데이터 파일로 저장 - 클라이언트가 서버에 요청할 때 자동으로 서버에 전송
		 * 세션 : 사용자 접속 정보를 서버에서 관리 - 클라이언트가 서버에 요청할 때 서버는 클라이언트를 구분할 수 있는 ID를 생성해 클라이언트에게 전달
		 */
		System.out.println("[AdminMemberController] loginConfirm()");

		String nextPage = "redirect:/admin/member/login_ok"; // 기본 페이지를 로그인 성공으로 지정
		// 같은 웹 페이지 2개를 열어 하나 로그아웃할 시 세션이 만료되어 기존 페이지에서 새로고침 하면 로그아웃 되어야 하지만
		// PostMapping의 결과로 불러오는 login_ok는 새로고침으로 웹페이지 재 요청시 POST요청을 다시 전송함으로써 재 로그인 요청을 하기 때문에
		// 한 번 로그인 하고 그 결과를 새로운 Get 요청으로 login 성공 or 실패 페이지를 받아온다.

		AdminMemberVo loginedAdminMemberVo = adminMemberService.loginConfirm(adminMemberVo);
		// 로그인 성공 시 클라이언트에게 전송해줄 관리자 정보를 담은 Vo객체(loginedAdminMemberVo)를 생성한다.
		// adminMemberService의 loginConfirm 메서드 사용 - 로그인 성공 시 사용자 정보를 담은 adminMemberVo 객체 반환, 실패 시 빈 객체
		// 해당 adminMemberVo 객체는 login_form.jsp에서 매핑된 아이디와 비밀번호 정보를 담고 있다.


		if (loginedAdminMemberVo == null) { // 반환받은 객체가 null이라면
			nextPage = "redirect:/admin/member/login_ng"; // 로그인 실패 페이지 매핑

		} else { // 반환받은 객체가 존재한다면
			session.setAttribute("loginedAdminMemberVo", loginedAdminMemberVo);
			// 세션에 객체의(관리자의) 정보를 저장한다.
			// setAttribute(String name, Object value) : 데이터 값의 타입이 Object이기 때문에 어떤 데이터 타입도 저장가능
			session.setMaxInactiveInterval(60 * 30);
			// 세션의 유효시간 지정
			// 기준 = 초
			// 60 * 30 초 = 30분; 해당 세션은 30분간 유지되고 브라우저가 30분간 동작이 없으면 세션은 종료

		}

		return nextPage; // 실패라면 if문에 의해 ng 페이지 / 성공이라면 원래대로 ok 페이지로 매핑

	}
	@GetMapping("login_ok")
	public String loginOk() {
		return "admin/member/login_ok"; // 로그인 성공 페이지
	}
	@GetMapping("login_ng")
	public String loginNg() {
		return "admin/member/login_ng"; // 로그인 실패 페이지
	}

	/*
	 * 로그아웃 확인
	 */
//	@RequestMapping(value = "/logoutConfirm", method = RequestMethod.GET)
	@GetMapping("/logoutConfirm") // logoutConfirm으로 오는 Get 요청 처리 - nav.jsp에서 로그아웃에 하이퍼링크로 연결
	public String logoutConfirm(HttpSession session) { // 로그아웃 처리 메서드
		System.out.println("[AdminMemberController] logoutConfirm()");

		String nextPage = "redirect:/admin"; // 초기 페이지로 재연결

//		session.removeAttribute("loginedAdminMemberVo");
		session.invalidate(); // session의 시간을 만료하여 세션을 무효화시킨다.
		// 해당 메서드를 통해 세션에 저장된 데이터 loginedAdminMemberVo를 사용할 수 없게 된다.

		return nextPage; // 초기페이지로 연결(세션이 만료되어 초기 상태의 페이지 admin/home.jsp가 나오게 된다.

	}

	/*
	 * 관리자 목록(Model 사용)
	 */
//	@RequestMapping(value = "/listupAdmin", method = RequestMethod.GET)
//	// listupAdmin으로 오는 Get 요청 처리 - nav.jsp에서 관리자목록에 하이퍼링크로 연결
//	public String listupAdmin(Model model) { // 빈 모델 객체를 불러온다. (데이터 전송간 사용할 데이터를 담을 객체)
//		System.out.println("[AdminMemberController] modifyAccountConfirm()");
//
//		String nextPage = "admin/member/listup_admins";
//
//		List<AdminMemberVo> adminMemberVos = adminMemberService.listupAdmin();
//		// 	AdminMemberVo 객체를 담고 있는 리스트 선언
//		//	서비스의 listupAdmin() 메서드 사용 - 데이터베이스에 존재하는 전체 값을 불러오는 메서드
//
//		model.addAttribute("adminMemberVos", adminMemberVos);
//		// model 객체에 해당 리스트를 담아서 전송
//		// listup_admins.jsp에서 model 객체의 이름(adminMemberVos)과 foreach문의 items와 이름을 매핑하여 값 전달
//		// 해당 items의 값(adminMemberVo) 하나하나 접근하여 값(객체의 필드) 추출
//
//		return nextPage;
//
//	}

	/*
	 * 관리자 목록(ModelAndView 사용)
	 */
	@RequestMapping(value = "/listupAdmin", method = RequestMethod.GET)
	public ModelAndView listupAdmin() {
		// ModelAndView : 뷰와 모델(데이터)을 하나의 객체에 담아서 전달할 때 사용하는 클래스
		System.out.println("[AdminMemberController] modifyAccountConfirm()");

		String nextPage = "admin/member/listup_admins"; // 값을 넘겨줄 페이지 지정

		List<AdminMemberVo> adminMemberVos = adminMemberService.listupAdmin();
		// AdminMembeVo 객체를 담고 있는 리스트 선언

		ModelAndView modelAndView = new ModelAndView();		// ① ModelAndView 객체를 생성한다.
		modelAndView.setViewName(nextPage);					// ② ModelAndView에 뷰를 지정
		modelAndView.addObject("adminMemberVos", adminMemberVos);	// ③ ModelAndView에 데이터를 추가한다.

		return modelAndView;								// ④ ModelAndView를 반환한다. - 지정된 View에 addObject로 추가한 model을 담아 전송

	}

	/*
	 * 관리자 승인
	 */
	@RequestMapping(value = "/setAdminApproval", method = RequestMethod.GET)
	//setAdminApproval로 가는 Get 요청을 처리하는 메서드 - listup_admins.jsp에서 approval_url이라는 이름으로 해당 경로 매핑중
	public String setAdminApproval(@RequestParam("a_m_no") int a_m_no) {
		// RequestParam : URL의 파라미터로 지정한 값을 입력받는다.
		// listup_admins.jsp 파일에서 choose로 지정하여 클릭시 해당 관리자의 a_m_no 값을 파라미터로 전달
		// 파라미터 이름을 RequestParam 어노테이션에 지정한 이름과 동일하게 맞추어 주어 해당 값이 해당 파라미터로 매핑될 수 있도록 알려줌
		System.out.println("[AdminMemberController] setAdminApproval()");

		String nextPage = "redirect:/admin/member/listupAdmin";
		// 승인 요청이 완료된 후 다시 관리자 목록을 봐 승인완료되었는지 확인 해야 하므로 리다이렉트

		adminMemberService.setAdminApproval(a_m_no);
		// 파라미터 값을 서비스의 setAdminApproval 메서드에 인수로 전달 - 인증을 처리해주는 메서드
		return nextPage;

	}

	/*
	 * 회원정보 수정
	 */
//	@RequestMapping(value = "/modifyAccountForm", method = RequestMethod.GET)
	@GetMapping("/modifyAccountForm")
	// modifyAccountForm으로 들어오는 Http Get 요청을 처리한다. - admin/include/nav.jsp에서 계정수정에 하이퍼링크로 요청중
	public String modifyAccountForm(HttpSession session) {
		// HttpSession을 인수로 받아 현재 관리자가 로그인되어 있는지 확인한다.
		System.out.println("[AdminMemberController] modifyAccountForm()");

		String nextPage = "admin/member/modify_account_form"; // 계정 정보 수정 폼을 저장해둔 jsp 파일 경로

		AdminMemberVo loginConfirmedAdminMemberVo = (AdminMemberVo) session.getAttribute("loginedAdminMemberVo");
//		// loginConfirm 메서드에서 세션에 loginedAdminMemberVo 객체를 "loginedAdminMemberVo라는 이름으로 저장하였다.
//		// 해당 객체를 getAttribute로 불러와 현재 지정한 AdminMemberVo의 객체인 loginConfirmedAdminMemberVo객체에 저장한다.
//		// 현재 세션에 저장되어있는 loginedAdminMemberVo객체의 값을 불러와 Form 태그의 Value 속성과 매핑시켜준다.
//		if (loginConfirmedAdminMemberVo == null)
//			// 만약 현재 세션에 해당 객체가 존재하지 않는다면
//			// 계정 수정을 눌러 하이퍼링크로 접속하기에 웹페이지에서 접근할 때는 로그인하지 않으면 해당 경로로 접근할 수 없지만,
//			// 실제 url 경로 localhost:8080/admin/member/modifyAccountForm으로 직접 접근하는 경우를 차단한다.
//			nextPage = "redirect:/admin/member/loginForm";
			// 로그인 요청 폼 페이지로 리다이렉트
		return nextPage; // 현재 세션에 로그인에 성공한 객체가 존재한다면 admin/member/modify_account_form화면을 띄워준다.

	}

	/*
	 * 회원정보 수정 확인
	 */
//	@RequestMapping(value = "/modifyAccountConfirm", method = RequestMethod.POST)
	@PostMapping("/modifyAccountConfirm")
	// 회원정보가 수정되었느지 확인하는 메서드
	// modify_account_form에서 계정 정보를 수정하고 form 제출시 해당 경로로 Post 요청을 날린다.

	public String modifyAccountConfirm(AdminMemberVo adminMemberVo, HttpSession session) {
		System.out.println("[AdminMemberController] modifyAccountConfirm()");

		String nextPage = "redirect:/admin/member/modify_account_ok";
		//loginConfirm과 마찬가지로 Post의 결과물을 페이지로 받아오는 것이 아닌 리다이렉트로 요청을 받아와
		// 다른 페이지의 로그아웃 시 현재 페이지에서 수정 재요청을 보내 로그인 세션이 유지되는 경우를 없애준다.

		int result = adminMemberService.modifyAccountConfirm(adminMemberVo);
		// adminMemberService의 modifyAccountConfirm() 메서드 호출
		// modify_account_form에서 수정한 계정 정보의 name 속성과 여기서 주입한 빈 객체 adminMemberVo 객체의 필드를 매핑시켜
		// adminMemberVo객체의 필드 값을 채워 서비스의 modifyAccountConfirm 메서드의 매개변수로 전달한다.

		if (result > 0) { // update 성공 시; jdbcTemplate.update의 결과 값으로 인해 result = 1
			AdminMemberVo loginedAdminMemberVo = adminMemberService.getLoginedAdminMemberVo(adminMemberVo.getA_m_no());
			// adminMemberService의 getLoginedAdminMemberVo 메서드 호출
			// getLoginedAdminMemberVo : 데이터베이스에서 업데이트된 해당 데이터를 select하여 반환한다.
			// form에서 name이 a_m_no인 필드와 바인딩된 adminMemberVo객체의 a_m_no 필드 값을 get메서드로 불러와 인수로 요청한다.

			// 해당 데이터가 데이터베이스에 존재하지 않는다면 null 반환 / session에 null을 저장하므로 로그인 상태 유지 불가능
			// 앞선 update가 성공해야 해당 메서드가 실행되므로 a_m_no의 값을 가지는 데이터가 존재하지 않는 경우는 사실상 없다.

			session.setAttribute("loginedAdminMemberVo", loginedAdminMemberVo);
			// 해당 객체를 세션에 저장한다. - 세션에 저장되어 있는 객체 loginedAdminMemberVo를 업데이트 한다.
			session.setMaxInactiveInterval(60 * 30);
			// 세션의 만료시간 지정

		} else { // 업데이트 실패 시
			nextPage = "redirect:/admin/member/modify_account_ng";
			// 업데이트 실패 화면으로 리다이렉트한다.
			// 재 Post 요청을 막기위해 Get요청으로 리다이렉트
		}

		return nextPage;

	}

	@GetMapping("modify_account_ok")
	public String modifyAccountOk() {
		return "admin/member/modify_account_ok"; // 계정 수정 성공 페이지
	}
	@GetMapping("modify_account_ng")
	public String modifyAccountNg() {
		return "admin/member/modify_account_ng"; // 계정 수정 실패 페이지
	}

	/*
	 * 비밀번호 찾기
	 */
//	@RequestMapping(value = "/findPasswordForm", method = RequestMethod.GET)
	@GetMapping("/findPasswordForm")
	// findPasswordForm으로 오는 Get 요청 처리
	// login_form.jsp에서 find password의 하이퍼링크로 요청 중
	public String findPasswordForm() {
		System.out.println("[AdminMemberController] findPasswordForm()");

		String nextPage = "admin/member/find_password_form";
		// find_password_form.jsp를 매핑하여 클라이언트에게 전달

		return nextPage;

	}

	/*
	 * 비밀번호 찾기 확인
	 */
//	@RequestMapping(value = "/findPasswordConfirm", method = RequestMethod.POST)
	@PostMapping("/findPasswordConfirm")
	public String findPasswordConfirm(AdminMemberVo adminMemberVo) {
		// 비밀번호 찾기 Post 요청을 받아 성공과 실패를 나누는 메서드
		// adminMemberVo 객체를 매개변수로 사용한다.
		// find_password_form에서 name에 지정한 a_m_id, a_m_name, a_m_mail을 adminMemberVo객체의 같은 이름을 가진 필드에 바인딩 하여 Post 요청을 전송한다.
		System.out.println("[AdminMemberController] findPasswordConfirm()");

		String nextPage = "admin/member/find_password_ok";
		// 세션문제를 해결하기 위해 로그인, 계정수정과 마찬가지로 get 요청으로 리다이렉트 해준다.

		int result = adminMemberService.findPasswordConfirm(adminMemberVo);
		// 서비스의 findPasswordConfirm 메서드 호출
		// findPasswordConfirm : form에서 전달한 값으로 데이터를 불러와 해당 데이터의 비밀번호를 새로운 비밀번호로 업데이트한다.
		// 업데이트 성공 시 1, 실패 시 -1 반환
		if (result <= 0) // 새로운 비밀번호 업데이트 실패시
			nextPage = "admin/member/find_password_ng";
			// find_password_ng 페이지로 리다이렉트

		return nextPage;

	}
}