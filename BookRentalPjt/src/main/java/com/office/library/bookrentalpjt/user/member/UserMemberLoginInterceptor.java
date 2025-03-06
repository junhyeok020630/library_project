package com.office.library.bookrentalpjt.user.member;




import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserMemberLoginInterceptor implements HandlerInterceptor {


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession(false);
		if (session != null) {
			Object object = session.getAttribute("loginedUserMemberVo");
			
			if (object != null)
				return true;
			
		}
		
		response.sendRedirect(request.getContextPath() + "/user/member/loginForm");
		
		return false;
		
	}
	
}
