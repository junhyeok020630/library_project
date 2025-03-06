package com.office.library.bookrentalpjt.book.admin;

import java.util.List;


import com.office.library.bookrentalpjt.book.BookVo;
import com.office.library.bookrentalpjt.book.HopeBookVo;
import com.office.library.bookrentalpjt.book.RentalBookVo;
import com.office.library.bookrentalpjt.book.admin.util.UploadFileService;
import com.office.library.bookrentalpjt.admin.member.AdminMemberVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/book/admin")
public class BookController {

	@Autowired
	BookService bookService; // 책관련 로직 메서드가 정리 되어있는 BookService 의존성 주입
	
	@Autowired
	UploadFileService uploadFileService; // 파일 업로드를 위한 메서드가 저장 되어있는 UploadFileService 의존성 주입
	
	/*
	 * 도서 등록
	 */
//	@RequestMapping(value = "/registerBookForm", method = RequestMethod.GET)
	@GetMapping("/registerBookForm")
	// 도서 등록 화면으로 이동
	// registerBookForm의 Get요청 처리
	// nav.jsp의 도서 등록에 하이퍼링크로 요청 중
	public String registerBookForm() {
		System.out.println("[BookController] registerBookForm()");
		
		String nextPage = "admin/book/register_book_form";
		
		return nextPage; // register_book_form.jsp 화면을 반환
		
	}
	
	/*
	 * 도서 등록 확인
	 */
//	@RequestMapping(value = "/registerBookConfirm", method = RequestMethod.POST)
	@PostMapping("/registerBookConfirm")
	// 도서 등록 확인 페이지 요청 등록 성공시 ok 실패시 ng로 매핑
	// 관리자 정보를 담은 객체를 Session에 저장하는 메서드가 없으므로 재 Post에 의한 Session 문제가 일어나지 않아 Get으로 리다이렉트 해주지 않아도 된다.
	public String registerBookConfirm(BookVo bookVo,
									  @RequestParam("file") MultipartFile file) {
		// 매개변수로 BookVo 객체와 파라미터로 file을 전달받는다.
		// BookVo는 form에서 input의 name 속성과 객체의 필드가 바인딩 되어 매개변수에 전달해준다.
		// name 속성이 file인 속성을 URL 파라미터에 담아 Post를 요청한다.
		System.out.println("[BookController] registerBookConfirm()");
		
		String nextPage = "admin/book/register_book_ok"; // 도서 등록 성공 페이지
		
		// SAVE FILE
		String savedFileName = uploadFileService.upload(file);
		// uploadFileService의 upload(file) 메서드로 저장된 파일 이름을 불러온다.
		// upload(file) : 업로드한 파일을 서버에 저장하고 파일의 이름과 확장자를 반환하는 메서드
		
		if (savedFileName != null) { // 파일이 존재한다면
			bookVo.setB_thumbnail(savedFileName); // Form에서 입력 받은 bookVo 객체의 b_thumbnail값을 set메서드로 넣어준다.
			int result = bookService.registerBookConfirm(bookVo);
			// bookService의 registerBookConfirm 메서드 사용
			// registerBookConfirm(bookVo) : 해당 Vo 객체를 DAO의 sql문(insert)문과 매핑해주고 결과를 반환한다.
			// 성공 시 1 / 실패 시 0 or -1 반환
			
			if (result <= 0)
				nextPage = "admin/book/register_book_ng"; // 실패 페이지
			
		} else {
			nextPage = "admin/book/register_book_ng"; // 성공 페이지
		}
		
		return nextPage;
		
	}
	
	/*
	 * 도서 검색
	 */
//	@RequestMapping(value = "/searchBookConfirm", method = RequestMethod.GET)
	@GetMapping("/searchBookConfirm")
	// 도서 검색 확인 요청 처리 메서드
	// nav.jsp의 form 태그로 연결 - 해당 URL로 GET 요청
	public String searchBookConfirm(BookVo bookVo, Model model) {
		// form 내부의 input 태그에 name 속성의 b_name과 bookVo객체의 b_name을 바인딩 하여 해당 메서드의 매개변수로 전달한다.
		// 검색 결과를 담아서 클라이언트에게 전송해줄 model 객체를 매개변수로 전달
		System.out.println("[UserBookController] searchBookConfirm()");
		
		String nextPage = "admin/book/search_book"; // 검색 결과 페이지로 화면을 넘겨준다. search_book.jsp
		
		List<BookVo> bookVos = bookService.searchBookConfirm(bookVo);
		// 서비스의 searchBookConfirm 메서드로 데이터베이스에 검색 결과의 데이터를 리스트로 받아온다.
		// searchBookConfirm(bookVo) : bookVo의 b_name 필드를 Dao에 전달; Dao가 select 메서드를 실행하고 결과값을 List로 변환; Service가 해당 결과를 컨트롤러에 반환
		
		model.addAttribute("bookVos", bookVos);
		// model 객체에 결과를 담아 클라이언트에게 전송
		
		return nextPage;
		
	}
	
	/*
	 * 도서 상세
	 */
//	@RequestMapping(value = "/bookDetail", method = RequestMethod.GET)
	@GetMapping("/bookDetail")
	// 도서 상세 페이지 요청 처리
	// bookDetail로 들어오는 Get 요청 처리
	// search_book.jsp : 검색 결과에서 도서 이름을 클릭하면 해당 url로 요청
	public String bookDetail(@RequestParam("b_no") int b_no, Model model) {
		// url 파라미터로 전송 받은 내용을 b_no 매개변수로 전달
		// 도서 이름 클릭 시 해당 도서의 b_no 값을 url 파라미터로 전달
		// 상세 페이지에 들어갈 데이터를 담을 Model 객체를 매개변수로 전달
		System.out.println("[BookController] bookDetail()");
		
		String nextPage = "admin/book/book_detail"; // 도서 상세 페이지로 화면 이동
		
		BookVo bookVo = bookService.bookDetail(b_no);
		// 파라미터로 전달받은 값을 bookService의 bookDetail메서드의 매개변수로 전달
		// bookDetail(int b_no) : b_no로 Dao에 전달해 select 문 실행; Dao가 select 결과 데이터를 bookVo 객체로 변환하여 전송; Service가 결과를 컨트롤러에 전송
		
		model.addAttribute("bookVo", bookVo);
		// model 객체에 결과값을 담은 객체를 담아 전송
		
		return nextPage;
		
	}
	
	/*
	 * 도서 수정
	 */
//	@RequestMapping(value = "/modifyBookForm", method = RequestMethod.GET)
	@GetMapping("/modifyBookForm")
	// modifyBookForm으로 들어오는 Get 요청 처리
	// book_detail.jsp에서 도서 수정 버튼으로 해당 경로 요청
	public String modifyBookForm(@RequestParam("b_no") int b_no, 
								 Model model, 
								 HttpSession session) {
		// 도서 상세 페이지에서 전달한 bookVo 객체의 b_no 값을
		// 도서 수정 버튼클릭 시 url 파라미터로 전송한다.
		// 수정할 도서 객체 (select 결과물)를 담을 Model 객체를 매개변수로 전달
		// 도서 관리자의 로그인 세션 만료시 수정이 불가능 하도록 HttpSession을 매개변수로 전달
		System.out.println("[BookController] bookDetail()");
		
		String nextPage = "admin/book/modify_book_form"; // modify_book_form.jsp로 화면 이동
		
		AdminMemberVo loginedAdminMemberVo = (AdminMemberVo) session.getAttribute("loginedAdminMemberVo");
		// 관리자 정보를 담은 객체를 세션에서 가져온다.
//		if (loginedAdminMemberVo == null) // 만약 해당 세션이 만료되었다면
//			return "redirect:/admin/member/loginForm"; // 다시 로그인을 요청하는 폼으로 이동

		// 세션이 만료되지 않았으면
		BookVo bookVo = bookService.modifyBookForm(b_no);
		// 서비스의 modifyBookForm 메서드 실행
		// 파라미터로 받은 b_no 값을 매개변수로 전달
		// modifyBookForm : Dao에 b_no 값을 전달해 Dao에서 해당 값을 가진 데이터를 select해 그 결과값(bookVo)을 컨트롤러로 반환하는 메서드
		
		model.addAttribute("bookVo", bookVo);
		// model 객체에 수정할 bookVo 객체를 담아 같이 전송
		
		return nextPage;
		
	}
	
	/*
	 * 도서 수정 확인
	 */
//	@RequestMapping(value = "/modifyBookConfirm", method = RequestMethod.POST)
	@PostMapping("/modifyBookConfirm")
	// 도서 수정 확인 요청 처리 메서드
	public String modifyBookConfirm(BookVo bookVo, 
									@RequestParam("file") MultipartFile file, 
									HttpSession session) {
		// form에서 select의 결과물을 담은 수정할 bookVo 객체를 매개변수로 전달
		// file을 파라미터로 전송 / 파일 업로드시 파라미터로 같은 name의 파일 값 전달
		// 관리자의 세션이 만료되었으면 수정이 불가능 하도록 HttpSession을 매개변수로 전달받는다.
		System.out.println("[BookController] modifyBookConfirm()");
		
		String nextPage = "admin/book/modify_book_ok"; // 기본 페이지 수정 성공 페이지
		
		AdminMemberVo loginedAdminMemberVo = (AdminMemberVo) session.getAttribute("loginedAdminMemberVo");
//		// 현재 로그인 세션 상태의 loginedAdminMemberVo 값을 불러온다.
//		// 해당 값이 존재하면 loginedAdminMemberVo 객체가 반환되고, 없으면 null 반환
//		if (loginedAdminMemberVo == null) // null이라면(세션이 만료되었으면)
//			return "redirect:/admin/member/loginForm"; // 로그인 폼으로 리다이렉트
		
		if (!file.getOriginalFilename().equals("")) {
			//관리자가 표지 이미지를 변경했는지(새 이미지 파일 업로드) 확인하는 if 문
			// 파일의 이름이 존재한다면, 새로운 파일이 저장

			// SAVE FILE
			String savedFileName = uploadFileService.upload(file);
			// upload(file) : 해당 파일을 서버에 업로드하고, 변경된 파일명을 반환한다.
			if (savedFileName != null) // 파일명이 존재한다면
				bookVo.setB_thumbnail(savedFileName); // 수정된 도서 객체의 사진을 새로 저장된 사진으로 변경한다.
			
		}
		
		int result = bookService.modifyBookConfirm(bookVo);
		// 수정 정보를 담은 객체를 bookService의 modifyBookConfirm으로 전달한다.
		// modifyBookConfirm() : bookDao의 updateBook의 실행결과를 컨트롤러로 반환해준다.(성공 시 1 / 실패 시 -1)

		
		if (result <= 0)
			nextPage = "admin/book/modify_book_ng";
		
		return nextPage;
		
	}
	
	/*
	 * 도서 삭제 확인
	 */
//	@RequestMapping(value = "/deleteBookConfirm", method = RequestMethod.GET)
	@GetMapping("/deleteBookConfirm")
	// 도서 삭제 GET 요청 확인 처리 메서드
	public String deleteBookConfirm(@RequestParam("b_no") int b_no, 
									HttpSession session) {
		// book_detail.jsp의 도서 삭제 버튼 클릭시
		// 해당 페이지에서 들고 있던 도서 객체의 b_no 값을 URL 파라미터로 전송
		// 관리자 세션 만료시 도서를 삭제할 수 없도록 HttpSession 클래스 주입
		System.out.println("[BookController] deleteBookConfirm()");
		
		String nextPage = "admin/book/delete_book_ok";
		// 기본 다음 페이지를 삭제 성공 페이지로 지정
		
		AdminMemberVo loginedAdminMemberVo = (AdminMemberVo) session.getAttribute("loginedAdminMemberVo");
//		// 세션에 저장된 loginedAdminMemberVo 객체를 불러옴
//		if (loginedAdminMemberVo == null) // 해당 객체가 null이라면 ( 세션이 만료되었으면)
//			return "redirect:/admin/member/loginForm"; // 로그인 폼으로 리다이렉트
		
		int result = bookService.deleteBookConfirm(b_no);
		// bookService의 deleteBookConfirm 메서드에 파라미터의 b_no 값 전송
		// deleteBookConfirm() : Dao의 deleteBook의 결과값을 컨트롤러에 반환하는 메서드 ( 성공 시 1 / 실패 시 -1 반환)
		
		if (result <= 0) // 삭제 실패 시
			nextPage = "admin/book/delete_book_ng"; // 삭제 요청 실패 페이지로 이동
		
		return nextPage;
		
	}

	/*
	 * 대출 도서 목록
	 */

	//	@RequestMapping(value = "/getRentalBooks", method = RequestMethod.GET)
	@GetMapping("/getRentalBooks")
	public String getRentalBooks(Model model) {
		System.out.println("[BookController] getRentalBooks()");

		String nextPage = "admin/book/rental_books";

		List<RentalBookVo> rentalBookVos = bookService.getRentalBooks();

		model.addAttribute("rentalBookVos", rentalBookVos);

		return nextPage;

	}

	/*
	 * 도서 반납 확인
	 */
//	@RequestMapping(value = "/returnBookConfirm", method = RequestMethod.GET)
	@GetMapping("/returnBookConfirm")
	public String returnBookConfirm(@RequestParam("b_no") int b_no,
									@RequestParam("rb_no") int rb_no) {
		System.out.println("[BookController] returnBookConfirm()");

		String nextPage = "admin/book/return_book_ok";

		int result = bookService.returnBookConfirm(b_no, rb_no);

		if (result <= 0)
			nextPage = "admin/book/return_book_ng";

		return nextPage;

	}
	/*
	 * 희망 도서 목록
	 */
//	@RequestMapping(value = "/getHopeBooks", method = RequestMethod.GET)
	@GetMapping("/getHopeBooks")
	public String getHopeBooks(Model model) {
		System.out.println("[BookController] getHopeBooks()");

		String nextPage = "admin/book/hope_books";

		List<HopeBookVo> hopeBookVos = bookService.getHopeBooks();

		model.addAttribute("hopeBookVos", hopeBookVos);

		return nextPage;

	}

	/*
	 * 희망 도서 등록(입고 처리)
	 */
//	@RequestMapping(value = "/registerHopeBookForm", method = RequestMethod.GET)
	@GetMapping("/registerHopeBookForm")
	public String registerHopeBookForm(Model model, HopeBookVo hopeBookVo) {
		System.out.println("[BookController] registerHopeBookForm()");

		String nextPage = "admin/book/register_hope_book_form";

		model.addAttribute("hopeBookVo", hopeBookVo);

		return nextPage;

	}

	/*
	 * 희망 도서 등록(입고 처리) 확인
	 */
//	@RequestMapping(value = "/registerHopeBookConfirm", method = RequestMethod.POST)
	@PostMapping("/registerHopeBookConfirm")
	public String registerHopeBookConfirm(BookVo bookVo,
										  @RequestParam("hb_no") int hb_no,
										  @RequestParam("file") MultipartFile file) {
		System.out.println("[BookController] registerHopeBookConfirm()");

		System.out.println("hb_no: " + hb_no);

		String nextPage = "admin/book/register_book_ok";

		// SAVE FILE
		String savedFileName = uploadFileService.upload(file);

		if (savedFileName != null) {
			bookVo.setB_thumbnail(savedFileName);
			int result = bookService.registerHopeBookConfirm(bookVo, hb_no);

			if (result <= 0)
				nextPage = "admin/book/register_book_ng";

		} else {
			nextPage = "admin/book/register_book_ng";

		}

		return nextPage;

	}

	/*
	 * 전체 도서 목록
	 */
//	@RequestMapping(value = "/getAllBooks", method = RequestMethod.GET)
	@GetMapping("/getAllBooks")
	public String getAllBooks(Model model) {
		System.out.println("[BookController] getAllBooks()");

		String nextPage = "admin/book/full_list_of_books";

		List<BookVo> bookVos = bookService.getAllBooks();

		model.addAttribute("bookVos", bookVos);

		return nextPage;

	}

}
