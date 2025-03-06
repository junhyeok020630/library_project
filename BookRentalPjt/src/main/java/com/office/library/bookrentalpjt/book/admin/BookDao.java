package com.office.library.bookrentalpjt.book.admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.office.library.bookrentalpjt.book.BookVo;
import com.office.library.bookrentalpjt.book.HopeBookVo;
import com.office.library.bookrentalpjt.book.RentalBookVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;


@Component
public class BookDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public boolean isISBN(String b_isbn) {
		// 동일한 ISBN을 가지는 도서가 DB에 존재하는지 확인하는 메서드
		System.out.println("[BookDao] isISBN()");
		
		String sql =  "SELECT COUNT(*) FROM tbl_book "
					+ "WHERE b_isbn = ?";
		
		int result = jdbcTemplate.queryForObject(sql, Integer.class, b_isbn);
		// SQL의 결과값(쿼리문 결과 개수)을 Integer로 받는다. - 앞선 SQL문에서 Count를 실행함
		
		return result > 0 ? true : false;
		// 결과의 개수가 0보다 크면 true / 아니면 false 반환
	}
	
	public int insertBook(BookVo bookVo) {
		// bookVo 객체를 매개변수로 받아 해당 필드 값을 바탕으로 데이터 베이스에 값을 삽입하는 메서드
		System.out.println("[BookDao] insertBook()");
		
		String sql = "INSERT INTO tbl_book(b_thumbnail, "
										+ "b_name, "
										+ "b_author, "
										+ "b_publisher, "
										+ "b_publish_year, "
										+ "b_isbn, "
										+ "b_call_number, "
										+ "b_rantal_able, "
										+ "b_reg_date, "
										+ "b_mod_date) "
										+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
		
		int result = -1;
		
		try {
			
			result = jdbcTemplate.update(sql,
											bookVo.getB_thumbnail(), 
											bookVo.getB_name(),
											bookVo.getB_author(),
											bookVo.getB_publisher(),
											bookVo.getB_publish_year(),
											bookVo.getB_isbn(),
											bookVo.getB_call_number(),
											bookVo.getB_rantal_able()
											);
			// update : 영향 받은 행의 수 반환
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return result; // 성공 시 영향 받은 행의 수 (아마도 1) 반환 / 실패 시 -1 반환
		
	}
	
	public List<BookVo> selectBooksBySearch(BookVo bookVo) {
		// b_name 값을 들고있는 bookVo 객체를 매개변수로 받는다.
		System.out.println("[BookDao] selectBooks()");
		
		String sql =  "SELECT * FROM tbl_book "
					+ "WHERE b_name LIKE ? "
					+ "ORDER BY b_no DESC";
		// like를 사용하여 해당 이름을 가지고 있는 데이터를 모두 출력
		List<BookVo> bookVos = null;
		// 결과 값을 저장할 리스트
		try {
			
			bookVos = jdbcTemplate.query(sql, new RowMapper<BookVo>() {

				@Override
				public BookVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					
					BookVo bookVo = new BookVo();
					
					bookVo.setB_no(rs.getInt("b_no"));
					bookVo.setB_thumbnail(rs.getString("b_thumbnail"));
					bookVo.setB_name(rs.getString("b_name"));
					bookVo.setB_author(rs.getString("b_author"));
					bookVo.setB_publisher(rs.getString("b_publisher"));
					bookVo.setB_publish_year(rs.getString("b_publish_year"));
					bookVo.setB_isbn(rs.getString("b_isbn"));
					bookVo.setB_call_number(rs.getString("b_call_number"));
					bookVo.setB_rantal_able(rs.getInt("b_rantal_able"));
					bookVo.setB_reg_date(rs.getString("b_reg_date"));
					bookVo.setB_mod_date(rs.getString("b_mod_date"));
					
					return bookVo;
					
				}
				
			}, "%" + bookVo.getB_name() + "%");
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return bookVos.size() > 0 ? bookVos : null;
		// 결과 리스트의 크기가 0보다 크면 해당 리스트 반환 / 아니면 null 반환
	}
	
	public BookVo selectBook(int b_no) {
		// where 문에 b_no를 넣어 select 결과값 출력
		System.out.println("[BookDao] selectBook()");
		
		String sql = "SELECT * FROM tbl_book WHERE b_no = ?";
		
		List<BookVo> bookVos = null;
		// SQL 결과값을 저장할 List
		try {
			
			bookVos = jdbcTemplate.query(sql, new RowMapper<BookVo>() {
				// query : List<BookVo> 반환
				@Override
				public BookVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					
					BookVo bookVo = new BookVo();
					
					bookVo.setB_no(rs.getInt("b_no"));
					bookVo.setB_thumbnail(rs.getString("b_thumbnail"));
					bookVo.setB_name(rs.getString("b_name"));
					bookVo.setB_author(rs.getString("b_author"));
					bookVo.setB_publisher(rs.getString("b_publisher"));
					bookVo.setB_publish_year(rs.getString("b_publish_year"));
					bookVo.setB_isbn(rs.getString("b_isbn"));
					bookVo.setB_call_number(rs.getString("b_call_number"));
					bookVo.setB_rantal_able(rs.getInt("b_rantal_able"));
					bookVo.setB_reg_date(rs.getString("b_reg_date"));
					bookVo.setB_mod_date(rs.getString("b_mod_date"));
					
					return bookVo;
					
				}
				
			}, b_no);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return bookVos.size() > 0 ? bookVos.get(0) : null;
		// 결과 리스트의 크기가 0보다 크면 해당 리스트의 첫번째 값 반환 / 아니면 null 반환
	}
	
	public int updateBook(BookVo bookVo) {
		// 수정정보를 담은 bookVo 객체를 매개변수로 받아온다.
		System.out.println("[BookDao] updateBook()");
		
		List<String> args = new ArrayList<String>();
		// 수정 정보를 List에 담아 저장해둔다.
		// 각 칼럼에 올바른 데이터를 전달해주기 위해
		
		String sql =  "UPDATE tbl_book SET ";
		// 수정을 실행하는 update 쿼리문
		// 사진이 있을 때와 없을 때의 쿼리문이 다르기 때문에 SET까지만 작성하고 상황에 맞게 다른 쿼리문을 아래 로직에서 추가한다.

			   if (bookVo.getB_thumbnail() != null) {
				   // 업로드한 사진이 존재한다면
				   sql += "b_thumbnail = ?, ";
				   // b_thumnail을 SET에 넣어 변경값으로 지정 후
				   args.add(bookVo.getB_thumbnail());
				   // b_thumnail 변경 값을 args 리스트에 저장
			   }
			   
			   sql += "b_name = ?, ";
			   // 쿼리문의 SET에 b_name 칼럼 추가
			   args.add(bookVo.getB_name());
			   // args에 수정된 객체의 책 이름 저장
			   
			   sql += "b_author = ?, ";
			   // 쿼리문의 SET에 b_author 칼럼 추가
			   args.add(bookVo.getB_author());
			   // args에 수정된 객체의 책 저자 저장

			   sql += "b_publisher = ?, ";
			   // 쿼리문의 SET에 b_publisher 칼럼 추가
			   args.add(bookVo.getB_publisher());
			   // args에 수정된 객체의 책 출판사 저장
			   
			   sql += "b_publish_year = ?, ";
			   // 쿼리문의 SET에 b_publish_year 칼럼 추가
			   args.add(bookVo.getB_publish_year());
			   // args에 수정된 객체의 책 출판년도 추가
			   
			   sql += "b_isbn = ?, ";
			   args.add(bookVo.getB_isbn());
			   
			   sql += "b_call_number = ?, ";
			   args.add(bookVo.getB_call_number());
			   
			   sql += "b_rantal_able = ?, ";
			   args.add(Integer.toString(bookVo.getB_rantal_able()));
			   
			   sql += "b_mod_date = NOW() ";
			   // 수정 날짜는 현재 시간으로 변경
			   
			   sql += "WHERE b_no = ?";
			   // 변경할 데이터를 b_no 값으로 찾도로 쿼리문에 Where 추가
			   args.add(Integer.toString(bookVo.getB_no()));
			   // 조거문에 들어간 b-no = ?의 파라미터로 전송될 객체의 b_no값을 args에 추가
			   
		
		int result = -1; // 기본값 실패
		
		try {
			
			result = jdbcTemplate.update(sql, args.toArray());
			// update 결과, 영향받은 행의 개수를 result에 저장 => 1 / 실패 시 0
			//
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return result;
		// 데이터베이스 업데이트 성공 시 1 / 실패 시 0 or -1
		// 쿼리문이 실행 되었지만 실행 결과가 없을 경우 : 0
		// 쿼리문 자체가 프로그램 오류로 작동하지 않았을 경우 : -1
		
	}
	
	public int deleteBook(int b_no) {
		// 서비스로 부터 b_no 값을 매개변수로 받아온다.
		System.out.println("[BookDao] deleteBook()");
		
		String sql =  "DELETE FROM tbl_book "
					+ "WHERE b_no = ?";
		// 도서 삭제 쿼리 ?에 받아온 매개변수를 넣을 수 있도록 쿼리 파라미터로 지정한다.
		int result = -1; // 기본 값 -1로 지정
		
		try {
			
			result = jdbcTemplate.update(sql, b_no);
			// 삭제를 수행하고 영향받은 행의 수 반환
			// 성공 : 1 / 쿼리문을 실행했지만 데이터가 변하지 않음 : 0 / 쿼리문이 실행되지 않음 : -1
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return result;
		
	}
	public List<RentalBookVo> selectRentalBooks() {
		System.out.println("[BookDao] selectRentalBooks()");

		String sql =  "SELECT * FROM tbl_rental_book rb "
				+ "JOIN tbl_book b "
				+ "ON rb.b_no = b.b_no "
				+ "JOIN tbl_user_member um "
				+ "ON rb.u_m_no = um.u_m_no "
				+ "WHERE rb.rb_end_date = '1000-01-01' "
				+ "ORDER BY um.u_m_id ASC, rb.rb_reg_date DESC";

		List<RentalBookVo> rentalBookVos = new ArrayList<RentalBookVo>();

		try {

			rentalBookVos = jdbcTemplate.query(sql, new RowMapper<RentalBookVo>() {

				@Override
				public RentalBookVo mapRow(ResultSet rs, int rowNum) throws SQLException {

					RentalBookVo rentalBookVo = new RentalBookVo();

					rentalBookVo.setRb_no(rs.getInt("rb_no"));
					rentalBookVo.setB_no(rs.getInt("b_no"));
					rentalBookVo.setU_m_no(rs.getInt("u_m_no"));
					rentalBookVo.setRb_start_date(rs.getString("rb_start_date"));
					rentalBookVo.setRb_end_date(rs.getString("rb_end_date"));
					rentalBookVo.setRb_reg_date(rs.getString("rb_reg_date"));
					rentalBookVo.setRb_mod_date(rs.getString("rb_mod_date"));

					rentalBookVo.setB_thumbnail(rs.getString("b_thumbnail"));
					rentalBookVo.setB_name(rs.getString("b_name"));
					rentalBookVo.setB_author(rs.getString("b_author"));
					rentalBookVo.setB_publisher(rs.getString("b_publisher"));
					rentalBookVo.setB_publish_year(rs.getString("b_publish_year"));
					rentalBookVo.setB_isbn(rs.getString("b_isbn"));
					rentalBookVo.setB_call_number(rs.getString("b_call_number"));
					rentalBookVo.setB_rantal_able(rs.getInt("b_rantal_able"));
					rentalBookVo.setB_reg_date(rs.getString("b_reg_date"));

					rentalBookVo.setU_m_id(rs.getString("u_m_id"));
					rentalBookVo.setU_m_pw(rs.getString("u_m_pw"));
					rentalBookVo.setU_m_name(rs.getString("u_m_name"));
					rentalBookVo.setU_m_gender(rs.getString("u_m_gender"));
					rentalBookVo.setU_m_mail(rs.getString("u_m_mail"));
					rentalBookVo.setU_m_phone(rs.getString("u_m_phone"));
					rentalBookVo.setU_m_reg_date(rs.getString("u_m_reg_date"));
					rentalBookVo.setU_m_mod_date(rs.getString("u_m_mod_date"));

					return rentalBookVo;

				}

			});

		} catch (Exception e) {
			e.printStackTrace();

		}

		return rentalBookVos;

	}

	public int updateRentalBook(int rb_no) {
		System.out.println("[BookDao] updateRentalBook()");

		String sql =  "UPDATE tbl_rental_book "
				+ "SET rb_end_date = NOW() "
				+ "WHERE rb_no = ?";

		int result = -1;

		try {

			result = jdbcTemplate.update(sql, rb_no);

		} catch (Exception e) {
			e.printStackTrace();

		}

		return result;
	}

	public int updateBook(int b_no) {
		System.out.println("[BookDao] updateRentalBook()");

		String sql =  "UPDATE tbl_book "
				+ "SET b_rantal_able = 1 "
				+ "WHERE b_no = ?";

		int result = -1;

		try {

			result = jdbcTemplate.update(sql, b_no);

		} catch (Exception e) {
			e.printStackTrace();

		}

		return result;

	}
	//	public List<HopeBookVo> selectHopeBooks() {
//		System.out.println("[BookDao] selectHopeBooks()");
//
//		String sql =  "SELECT * FROM tbl_hope_book hb "
//					+ "JOIN tbl_user_member um "
//					+ "ON hb.u_m_no = um.u_m_no "
//					+ "ORDER BY hb.hb_no DESC";
//
//		List<HopeBookVo> hopeBookVos = new ArrayList<HopeBookVo>();
//
//		try {
//
//			hopeBookVos = jdbcTemplate.query(sql, new RowMapper<HopeBookVo>() {
//
//				@Override
//				public HopeBookVo mapRow(ResultSet rs, int rowNum) throws SQLException {
//
//					HopeBookVo hopeBookVo = new HopeBookVo();
//
//					hopeBookVo.setHb_no(rs.getInt("hb_no"));
//					hopeBookVo.setHb_name(rs.getString("hb_name"));
//					hopeBookVo.setHb_author(rs.getString("hb_author"));
//					hopeBookVo.setHb_publisher(rs.getString("hb_publisher"));
//					hopeBookVo.setHb_publish_year(rs.getString("hb_publish_year"));
//					hopeBookVo.setHb_reg_date(rs.getString("hb_reg_date"));
//					hopeBookVo.setHb_mod_date(rs.getString("hb_mod_date"));
//					hopeBookVo.setHb_result(rs.getInt("hb_result"));
//					hopeBookVo.setHb_result_last_date(rs.getString("hb_result_last_date"));
//
//					hopeBookVo.setU_m_no(rs.getInt("u_m_no"));
//					hopeBookVo.setU_m_id(rs.getString("u_m_id"));
//					hopeBookVo.setU_m_pw(rs.getString("u_m_pw"));
//					hopeBookVo.setU_m_name(rs.getString("u_m_name"));
//					hopeBookVo.setU_m_gender(rs.getString("u_m_gender"));
//					hopeBookVo.setU_m_mail(rs.getString("u_m_mail"));
//					hopeBookVo.setU_m_phone(rs.getString("u_m_phone"));
//					hopeBookVo.setU_m_reg_date(rs.getString("u_m_reg_date"));
//					hopeBookVo.setU_m_reg_date(rs.getString("u_m_mod_date"));
//
//					return hopeBookVo;
//
//				}
//
//			});
//
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//
//		return hopeBookVos;
//
//	}

	public List<HopeBookVo> selectHopeBooks() {
		System.out.println("[BookDao] selectHopeBooks()");

		String sql =  "SELECT * FROM tbl_hope_book hb "
				+ "JOIN tbl_user_member um "
				+ "ON hb.u_m_no = um.u_m_no "
				+ "ORDER BY hb.hb_no DESC";

		List<HopeBookVo> hopeBookVos = new ArrayList<HopeBookVo>();

		try {

			RowMapper<HopeBookVo> rowMapper = BeanPropertyRowMapper.newInstance(HopeBookVo.class);
			hopeBookVos = jdbcTemplate.query(sql, rowMapper);

		} catch (Exception e) {
			e.printStackTrace();

		}

		return hopeBookVos;

	}

	public void updateHopeBookResult(int hb_no) {
		System.out.println("[BookDao] updateHopeBookResult()");

		String sql =  "UPDATE tbl_hope_book "
				+ "SET hb_result = 1, hb_result_last_date = NOW() "
				+ "WHERE hb_no = ?";

		try {

			jdbcTemplate.update(sql, hb_no);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

//	public List<BookVo> selectAllBooks() {
//		System.out.println("[BookDao] selectAllBooks()");
//
//		String sql =  "SELECT * FROM tbl_book "
//					+ "ORDER BY b_reg_date DESC";
//
//		List<BookVo> books = new ArrayList<BookVo>();
//
//		try {
//
//			books = jdbcTemplate.query(sql, new RowMapper<BookVo>() {
//
//				@Override
//				public BookVo mapRow(ResultSet rs, int rowNum) throws SQLException {
//
//					BookVo bookVo = new BookVo();
//
//					bookVo.setB_no(rs.getInt("b_no"));
//					bookVo.setB_thumbnail(rs.getString("b_thumbnail"));
//					bookVo.setB_name(rs.getString("b_name"));
//					bookVo.setB_author(rs.getString("b_author"));
//					bookVo.setB_publisher(rs.getString("b_publisher"));
//					bookVo.setB_publish_year(rs.getString("b_publish_year"));
//					bookVo.setB_isbn(rs.getString("b_isbn"));
//					bookVo.setB_call_number(rs.getString("b_call_number"));
//					bookVo.setB_rantal_able(rs.getInt("b_rantal_able"));
//					bookVo.setB_reg_date(rs.getString("b_reg_date"));
//					bookVo.setB_mod_date(rs.getString("b_mod_date"));
//
//					return bookVo;
//				}
//
//			});
//
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//
//		return books.size() > 0 ? books : null;
//
//	}

	public List<BookVo> selectAllBooks() {
		System.out.println("[BookDao] selectAllBooks()");

		String sql =  "SELECT * FROM tbl_book "
				+ "ORDER BY b_reg_date DESC";

		List<BookVo> books = new ArrayList<BookVo>();

		try {

			RowMapper<BookVo> rowMapper = BeanPropertyRowMapper.newInstance(BookVo.class);
			books = jdbcTemplate.query(sql, rowMapper);

		} catch (Exception e) {
			e.printStackTrace();

		}

		return books.size() > 0 ? books : null;

	}
}
