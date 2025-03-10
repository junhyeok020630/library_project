package com.office.library.bookrentalpjt.book.user;

import java.util.List;


import com.office.library.bookrentalpjt.book.HopeBookVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.office.library.bookrentalpjt.book.BookVo;
import com.office.library.bookrentalpjt.book.RentalBookVo;
import com.office.library.bookrentalpjt.user.member.UserMemberVo;

@Controller // book/admin/BookController와 빈 충돌 발생
// ConflictingBeanDefinitionException에라 발생 - 스프링 컨테이너에 빈을 등록할 때 클래스 이름의 첫 글자를 대문자로 변경해서 빈을 구분하는 ID로 사용하기 때문에
//@Controller("user.BookController") // 1. 빈의 아이디를 다르게 설정하여 해결 or 2. BeanNameGenerator 사용 - config/LibraryBeanNameGenerator
@RequestMapping("/book/user")
public class BookController {

	@Autowired
	BookService bookService;
	
	/*
	 * 도서 검색
	 */
//	@RequestMapping(value = "/searchBookConfirm", method = RequestMethod.GET)
	@GetMapping("/searchBookConfirm")
	public String searchBookConfirm(BookVo bookVo, Model model) {
		System.out.println("[UserBookController] searchBookConfirm()");
		
		String nextPage = "user/book/search_book";
		
		List<BookVo> bookVos = bookService.searchBookConfirm(bookVo);
		
		model.addAttribute("bookVos", bookVos);
		
		return nextPage;
		
	}
	
	/*
	 * 도서 상세
	 */
//	@RequestMapping(value = "/bookDetail", method = RequestMethod.GET)
	@GetMapping("/bookDetail")
	public String bookDetail(@RequestParam("b_no") int b_no, Model model) {
		System.out.println("[UserBookController] bookDetail()");
		
		String nextPage = "user/book/book_detail";
		
		BookVo bookVo = bookService.bookDetail(b_no);
		
		model.addAttribute("bookVo", bookVo);
		
		return nextPage;
		
	}
	
	/*
	 * 도서 대출
	 */
//	@RequestMapping(value = "/rentalBookConfirm", method = RequestMethod.GET)
	@GetMapping("/rentalBookConfirm")
	public String rentalBookConfirm(@RequestParam("b_no") int b_no, HttpSession session) {
		System.out.println("[UserBookController] rentalBookConfirm()");
		
		String nextPage = "user/book/rental_book_ok";
		
		UserMemberVo loginedUserMemberVo = 
				(UserMemberVo) session.getAttribute("loginedUserMemberVo");

//		if (loginedUserMemberVo == null)
//			return "redirect:/user/member/loginForm";
		// 로그인 세션이 만료되었을 경우 로그인 폼으로 리다이렉트
		// 인터셉터 인터페이스를 구현함으로써 리다이렉트를 줄일 수 있다.
		// 리다이렉트가 많아지면 동일 코드의 반복과 코드 유지 보수 효율성이 떨어지게 된다.
		// 인터셉터 클래스 : admin/member/AdminMemberLoginInterceptor | user/member/UserMemberLoginInterceptor
		// 위의 리다이렉트문과 동일한 기능은 user/member/UserMemberLoginInterceptor에 구현
		int result = bookService.rentalBookConfirm(b_no, loginedUserMemberVo.getU_m_no());
		
		if (result <= 0)
			nextPage = "user/book/rental_book_ng";
		
		return nextPage;
		
	}
	
	/*
	 * 나의 책장
	 */
//	@RequestMapping(value = "/enterBookshelf", method = RequestMethod.GET)
	@GetMapping("/enterBookshelf")
	public String enterBookshelf(HttpSession session, Model model) {
		System.out.println("[UserBookController] enterBookshelf()");
		
		String nextPage = "user/book/bookshelf";
		
		UserMemberVo loginedUserMemberVo = (UserMemberVo) session.getAttribute("loginedUserMemberVo");
		
		List<RentalBookVo> rentalBookVos = bookService.enterBookshelf(loginedUserMemberVo.getU_m_no());
		
		model.addAttribute("rentalBookVos", rentalBookVos);
		
		return nextPage;
	
	}
	
	/*
	 * 도서 대출 이력
	 */
//	@RequestMapping(value = "/listupRentalBookHistory", method = RequestMethod.GET)
	@GetMapping("/listupRentalBookHistory")
	public String listupRentalBookHistory(HttpSession session, Model model) {
		System.out.println("[UserBookController] listupRentalBookHistory()");
		
		String nextPage = "user/book/rental_book_history";
		
		UserMemberVo loginedUserMemberVo = (UserMemberVo) session.getAttribute("loginedUserMemberVo");
		
		List<RentalBookVo> rentalBookVos = bookService.listupRentalBookHistory(loginedUserMemberVo.getU_m_no());
		
		model.addAttribute("rentalBookVos", rentalBookVos);
		
		return nextPage;
		
	}
	/*
	 * 희망 도서 요청
	 */
//	@RequestMapping(value = "/requestHopeBookForm", method = RequestMethod.GET)
	@GetMapping("/requestHopeBookForm")
	public String requestHopeBookForm() {
		System.out.println("[UserBookController] requestHopeBookForm()");

		String nextPage = "user/book/request_hope_book_form";

		return nextPage;

	}

	/*
	 * 희망 도서 요청 확인
	 */
//	@RequestMapping(value = "/requestHopeBookConfirm", method = RequestMethod.GET)
	@GetMapping("/requestHopeBookConfirm")
	public String requestHopeBookConfirm(HopeBookVo hopeBookVo, HttpSession session) {
		System.out.println("[UserBookController] requestHopeBookConfirm()");

		String nextPage = "user/book/request_hope_book_ok";

		UserMemberVo loginedUserMemberVo = (UserMemberVo) session.getAttribute("loginedUserMemberVo");
		hopeBookVo.setU_m_no(loginedUserMemberVo.getU_m_no());

		int result = bookService.requestHopeBookConfirm(hopeBookVo);

		if (result <= 0)
			nextPage = "user/book/request_hope_book_ng";

		return nextPage;

	}

	/*
	 * 희망 도서 요청 목록
	 */
//	@RequestMapping(value = "/listupRequestHopeBook", method = RequestMethod.GET)
	@GetMapping("/listupRequestHopeBook")
	public String listupRequestHopeBook(HttpSession session, Model model) {
		System.out.println("[UserBookController] listupRequestHopeBook()");

		String nextPage = "user/book/list_hope_book";

		UserMemberVo loginedUserMemberVo = (UserMemberVo) session.getAttribute("loginedUserMemberVo");

		List<HopeBookVo> hopeBookVos =
				bookService.listupRequestHopeBook(loginedUserMemberVo.getU_m_no());

		model.addAttribute("hopeBookVos", hopeBookVos);

		return nextPage;

	}

}
