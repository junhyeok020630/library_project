package com.office.library.bookrentalpjt.admin.member;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminMemberLoginInterceptor implements HandlerInterceptor {
	// 해당 인터셉터를 사용할 요청 경로를 config/WebConfig에 정의

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// preHandle : 클라이언트의 요청이 컨트롤러에 전달하기전에 호출되고, 불린 반환
		// 매개 변수로 핸들러를 받아 true면 핸들러 실행 false면 핸들러 미실행
		// 회원인증 상태를 구분한 후 인증이 완료된 사용자한테만 웹 서비스를 제공함
		// 회원인증이 안된 사용자한테는 핸들러의 실행을 막고, 다른 페이지로 유도함

		HttpSession session = request.getSession(false); // 요청 세션 가져오기
		if (session != null) { // 세션이 존재한다면
			Object object = session.getAttribute("loginedAdminMemberVo");
			// 로그인 정보 객체 불러오기
			if (object != null) // 해당 객체가 존재한다면
				return true; // true 반한
			
		}
		// 세션이 만료되었거나, 세션에 로그인 객체가 존재하지 않는다면
		response.sendRedirect(request.getContextPath() + "/admin/member/loginForm"); // 로그인 폼으로 리다이렉트

		return false; // false 반환
		
	}
	// postHandle : 클라이언트의 요청이 컨트롤러에서 실행된 후 호출되고, 이 때 컨트롤러에서 예외가 발생되면 postHandle은 호출되지 않는다

	// afterCompletion : 클라이언트의 요청이 컨트롤러에서 실행되고, 뷰를 통해서 응답이 완료된 후 호출, 만약 뷰를 생성할 때 예외가 발생해도 afterCompletion은 호출됨
}
