package com.office.library.bookrentalpjt.book.admin;

import java.util.List;

import com.office.library.bookrentalpjt.book.BookVo;
import com.office.library.bookrentalpjt.book.HopeBookVo;
import com.office.library.bookrentalpjt.book.RentalBookVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BookService {

	final static public int BOOK_ISBN_ALREADY_EXIST = 0;	// 이미 등록된 도서
	final static public int BOOK_REGISTER_SUCCESS = 1;	// 신규 도서 등록 성공
	final static public int BOOK_REGISTER_FAIL = -1;	// 신규 도서 등록 실패
	
	@Autowired
	BookDao bookDao;

	public int registerBookConfirm(BookVo bookVo) {
		// 도서 등록 확인 요청 처리 메서드
		System.out.println("[BookService] registerBookConfirm()");
		
		boolean isISBN = bookDao.isISBN(bookVo.getB_isbn());
		// bookDao의 isISBN 메서드 사용
		// 동일한 ISBN이 DB에 있다면 true 없으면 false 반환
		
		if (!isISBN) { // DB에 동일한 ISBN이 없으면
			int result = bookDao.insertBook(bookVo);
			// 해당 도서를 DB에 삽입
			// 삽입 성공 시 1, 실패 시 0 반환
			if (result > 0)
				return BOOK_REGISTER_SUCCESS; // 등록 성공 상수 값 1 반환
			
			else
				return BOOK_REGISTER_FAIL; // 등록 실패 상수 값 -1 반환
			
		} else {
			return BOOK_ISBN_ALREADY_EXIST; // 동일한 도서 이미 존재 상수 값 0 반환
			
		}
		
	}
	
	public List<BookVo> searchBookConfirm(BookVo bookVo) {
		// 도서 검색 처리 메서드
		System.out.println("[BookService] searchBookConfirm()");
		
		return bookDao.selectBooksBySearch(bookVo);
		// Dao에 bookVo(b_name 값을 들고있는) 객체를 매개변수로 전달하고 그 결과값을 컨트롤러에 반환
	}
	
	public BookVo bookDetail(int b_no) {
		// 파라미터로 전달받은 b_no값을 매개변수로 받아
		System.out.println("[BookService] bookDetail()");
		
		return bookDao.selectBook(b_no);
		// Dao의 selectBook 메서드에 전달
		// selectBook(int b_no) : b_no를 조건문에 넣어 select 문 실행
		// 해당 결과값 모든 정보를 담은 bookVo 객체를 컨트롤러에 반환
	}
	
	public BookVo modifyBookForm(int b_no) {
		// 도서 수정 폼에 기본으로 들어가있을 입력 값을 데이터베이스에서 불러온다.
		System.out.println("[BookService] modifyBookForm()");
		
		return bookDao.selectBook(b_no);
		// 파라미터로 받아온 b_no 값으로 데이터베이스에서 select해 결과 데이터를 반환한다.
	}
	
	public int modifyBookConfirm(BookVo bookVo) {
		// 도서 수정 확인 메서드
		// 컨트롤러로부터 수정정보를 담은 BookVo 객체를 매개변수로 받는다.
		System.out.println("[BookService] modifyBookConfirm()");
		
		return bookDao.updateBook(bookVo);
		// 수정정보를 담은 bookVo 객체를 Dao의 updateBook() 메서드의 매개변수로 전달한다.
		// updateBook() : 데이터베이스 수정 쿼리문을 실행하고 해당 쿼리로 변경된 데이터의 개수를 반환한다.
		// 성공 시 1, 실패 시 0 or -1 반환
	}
	
	public int deleteBookConfirm(int b_no) {
		// 컨트롤러에서 URL 파라미터로 받아온 b_no 값을 매개변수로 받아온다.
		System.out.println("[BookService] deleteBookConfirm()");
		
		return bookDao.deleteBook(b_no);
		// 해당 b_no값을 Dao의 deleteBook() 메서드의 매개변수로 전달한다.
		// deleteBook(int b_no) : 해당 번호의 도서 데이터를 삭제하는 쿼리문을 실행하고 영향받은 행의 수를 반환하는 메서드( 성공 시 1 / 실패 시 0 or -1)
		
	}
	public List<RentalBookVo> getRentalBooks() {
		System.out.println("[BookService] getRentalBooks()");

		return bookDao.selectRentalBooks();

	}

	public int returnBookConfirm(int b_no, int rb_no) {
		System.out.println("[BookService] getRentalBooks()");

		int result = bookDao.updateRentalBook(rb_no);

		if (result > 0)
			result = bookDao.updateBook(b_no);

		return result;
	}

	public List<HopeBookVo> getHopeBooks() {
		System.out.println("[BookService] getHopeBooks()");

		return bookDao.selectHopeBooks();

	}

	public int registerHopeBookConfirm(BookVo bookVo, int hb_no) {
		System.out.println("[BookService] registerHopeBookConfirm()");

		boolean isISBN = bookDao.isISBN(bookVo.getB_isbn());

		if (!isISBN) {
			int result = bookDao.insertBook(bookVo);

			if (result > 0) {

				bookDao.updateHopeBookResult(hb_no);

				return BOOK_REGISTER_SUCCESS;

			} else {
				return BOOK_REGISTER_FAIL;

			}

		} else {
			return BOOK_ISBN_ALREADY_EXIST;

		}

	}

	public List<BookVo> getAllBooks() {
		System.out.println("[BookService] getAllBooks()");

		return bookDao.selectAllBooks();

	}


}
