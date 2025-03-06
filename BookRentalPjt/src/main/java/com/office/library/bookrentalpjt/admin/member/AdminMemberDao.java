package com.office.library.bookrentalpjt.admin.member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
// Dao : 데이터베이스를 직접적으로 다루는 메서드 정의
// Jpa 사용시 해당 레퍼지토리의 메서드를 불러와 사용하지만, 아래와 같이 직접적으로 SQL문을 작성하여 사용가능
public class AdminMemberDao {

	private final JdbcTemplate jdbcTemplate; // jdbc를 간단하게 사용하기 위해 스프링에서 제공하는 데이터베이스 접근 객체

	private final PasswordEncoder passwordEncoder;
	// 비밀번호 암호화를 위해 스프링 시큐리티에서 제공하는 인터페이스
	// SecurityConfig에서 해당 인터페이스의 구현체 BCryptPasswordEncoder를 스프링 빈으로 등록해두었다.
	
	public boolean isAdminMember(String a_m_id) { // 인수로 받은 id를 가지는 관리자가 존재하는지 확인하는 메서드
		System.out.println("[AdminMemberDao] isAdminMember()");
		
		String sql =  "SELECT COUNT(*) FROM tbl_admin_member " // tbl_admin_manager에 해당 id를 가지는 출력값의 count
				+ "WHERE a_m_id = ?"; // sql문 작성 ?에 id 값이 들어간다.
	
		int result = jdbcTemplate.queryForObject(sql, Integer.class, a_m_id);
		// 인수로 받은 a_m_id를 sql에 파라미터로 전송하고, 그 결과값 ( = count 값)을 정수로 반환한다.
		
		return result > 0 ? true : false; // count가 1개라도 있다면, ( = 해당 id를 가지는 관리자가 존재한다면) true / 아니면 false 반환
	
	}
	
	public int insertAdminAccount(AdminMemberVo adminMemberVo) { // form 태그와 매핑되어 값이 저장되어있는 Vo 객체를 인수로 불러온다.
		System.out.println("[AdminMemberDao] insertAdminAccount()");
		
		List<String> args = new ArrayList<String>(); // insert문의 values에 추가할 값(실제 데이터베이스에 추가할 값을 List 자료구조에 모아둔다.
		
		String sql =  "INSERT INTO tbl_admin_member(";
		 	   // a_m_no 필드의 경우 기본키로 지정 후 auto_increment를 지정하여 insert문에 직접 넣지 않아도 자동으로 1씩 증가하며 값이 추가될때 같이 추가된다.

		       if (adminMemberVo.getA_m_id().equals("super admin")) { // 만약 id가 최상위 관리자라면
				   sql += "a_m_approval, ";
				   // insert 쿼리문에 a_m_approval(인증정보)를 추가한다.
				   // -> 최상위 관리자만 추가하여 최상위 관리자일 때와 일반 관리자일때 실행되는 쿼리문이 달라진다.
				   args.add("1"); // 일반 관리자는 해당 값을 추가하지 않는다. 최상위 관리자만 회원가입시 인증정보를 받을 수 있다.
				   // args에 1을 추가한다. (DB의 a_m_approval 칼럼에 들어갈 값)
			   }
			   
			   sql += "a_m_id, "; // 쿼리문에 a_m_id를 추가한다.
			   args.add(adminMemberVo.getA_m_id()); // Vo객체(form으로 클라이언트에게 받아온 값)의 a_m_id 값을 args에 추가한다.(DB의 a_m_id 칼럼에 들어갈 값)

			   // 다른 값도 id와 동일하게 작동
			   sql += "a_m_pw, ";
			   args.add(passwordEncoder.encode(adminMemberVo.getA_m_pw()));
			   // 데이터베이스에 추가할 때 (args에 저장되는 값에)는 입력받은 비밀번호 값을 암호화하여 저장
			   
			   sql += "a_m_name, ";
			   args.add(adminMemberVo.getA_m_name());
			   
			   sql += "a_m_gender, ";
			   args.add(adminMemberVo.getA_m_gender());
			   
			   sql += "a_m_part, ";
			   args.add(adminMemberVo.getA_m_part());
			   
			   sql += "a_m_position, ";
			   args.add(adminMemberVo.getA_m_position());
			   
			   sql += "a_m_mail, ";
			   args.add(adminMemberVo.getA_m_mail());
			   
			   sql += "a_m_phone, ";
			   args.add(adminMemberVo.getA_m_phone());
			   
			   sql += "a_m_reg_date, a_m_mod_date) ";
			   
			   if (adminMemberVo.getA_m_id().equals("super admin")) // 최상위 관리자일 경우
				   sql += "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
			   		// a_m_approval(인증정보)값이 포함되므로 values에 들어가야 할 값이 하나 더 추가된다.
			   else 
				   sql += "VALUES(?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
			   		// 일반 관리자의 경우 최상위 관리자보다 하나 적은 값을 추가한다.
		int result = -1; // 기본을 실패로 지정
		
		try {
			
			result = jdbcTemplate.update(sql, args.toArray());
			// JdbcTemplate : SQL문을 실행한다음 영향 받은 행의 개수를 반환하는 메서드
			// args를 배열로 변환하여 메서드의 인수로 전달하면
			// 해당 sql의 ?에 파라미터로 배열의 값을 순서대로 전송한다.
			// 해당 insert문을 통해 1개의 행이 추가되므로 result는 1이된다.

			
		} catch (Exception e) { // 예외 발생 시
			e.printStackTrace(); // 예외 상세 정보 출력
			
		}
		
		return result; // 추가 성공 시 1 / 예외 발생 시 -1
		
	}

	public AdminMemberVo selectAdmin(AdminMemberVo adminMemberVo) {
		// id와 비밀번호를 담은 adminMemberVo객체를 인수로 받아와 해당 객체의 id 값으로 SQL문을 실행하고
		// 그 결과 값을 다시 adminmemberVo 객체에 담아 전송하는 방식
		System.out.println("[AdminMemberDao] selectAdmin()");

		String sql =  "SELECT * FROM tbl_admin_member "
				+ "WHERE a_m_id = ? AND a_m_approval > 0";
		// SQL 문 작성 ?에 파라미터로 get메서드로 불러온 Id값을 넣어준다.
		// 인증 정보가 1인 데이터만 불러온다 - 인증 정보가 0일 경우 최종 결과로 null 반환

		List<AdminMemberVo> adminMemberVos = new ArrayList<AdminMemberVo>(); // SQL 문의 결과값을 저장해둘 List 선언

		try {

			adminMemberVos = jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() { // 인터페이스 구현
				// query는 List를 반환하고 해당 List의 타입은 RowMapper에서 지정한 <AdminMemberVo> 제네릭 타입으로 반환타입을 지정한다.
				// query(sql, RowMapper, id) 해당 id를 sql의 파라미터에 전송해 결과값을 구하고 해당 결과값을 RowMapper의 타입으로 매핑시켜 반환


				@Override
				public AdminMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					// mapRow 메서드 : 결과값의 각 칼럼 값을 반환 객체와 매핑시켜주는 메서드
					// set 메서드로 각 필드의 값을 sql 결과값으로 채워준다.

					AdminMemberVo adminMemberVo = new AdminMemberVo(); // 빈 Vo 객체 생성

					adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
					adminMemberVo.setA_m_approval(rs.getInt("a_m_approval"));
					adminMemberVo.setA_m_id(rs.getString("a_m_id"));
					adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
					adminMemberVo.setA_m_name(rs.getString("a_m_name"));
					adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
					adminMemberVo.setA_m_part(rs.getString("a_m_part"));
					adminMemberVo.setA_m_position(rs.getString("a_m_position"));
					adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
					adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
					adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
					adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));

					return adminMemberVo; // set 메서드로 채워진 Vo 객체 반환

				}

			}, adminMemberVo.getA_m_id());

			if (!passwordEncoder.matches(adminMemberVo.getA_m_pw(), adminMemberVos.get(0).getA_m_pw()))
				// PasswordEncoder의 matches()메서드 : 암호화한 정보를 복호화하는 메서드
				// 복호화한 비밀번호가 일치하지 않다면
				adminMemberVos.clear(); // 리스트 제거 (리스트 내용 : null)

		} catch (Exception e) { // 예외 발생 시
			e.printStackTrace(); // 예외 상세 내용 출력

		}

		return adminMemberVos.size() > 0 ? adminMemberVos.get(0) : null;
		// 리스트에 내용이 존재한다면 리스트의 첫번째 adminMemberVo 반환
		// 리스트의 크기가 0이라면 null 반환
	}

	public List<AdminMemberVo> selectAdmins() {
		// 관리자 데이터베이스에 존재하는 모든 데이터를 List에 담아 반환하는 메서드
		System.out.println("[AdminMemberDao] selectAdmins()");

		String sql =  "SELECT * FROM tbl_admin_member";
		// 관리자 테이블의 전체 값을 조회하는 SQL문

		List<AdminMemberVo> adminMemberVos = new ArrayList<AdminMemberVo>();
		// 결과값을 저장한 List 선언

		try {

			adminMemberVos = jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {
			// sql의 결과문을 RowMapper 클래스의 mapRow메서드로 AdminMemberVo 객체에 하나하나 연결하여 값을 지정한 후 해당 리스트 반환
				@Override
				public AdminMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {

					AdminMemberVo adminMemberVo = new AdminMemberVo();

					adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
					adminMemberVo.setA_m_approval(rs.getInt("a_m_approval"));
					adminMemberVo.setA_m_id(rs.getString("a_m_id"));
					adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
					adminMemberVo.setA_m_name(rs.getString("a_m_name"));
					adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
					adminMemberVo.setA_m_part(rs.getString("a_m_part"));
					adminMemberVo.setA_m_position(rs.getString("a_m_position"));
					adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
					adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
					adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
					adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));

					return adminMemberVo;

				}

			});

		} catch (Exception e) { // 예외 발생 시
			e.printStackTrace(); // 예외 상세 내용 출력

		}

		return adminMemberVos; // 결과 값을 담고 있는 리스트 반환

	}

	public int updateAdminAccount(int a_m_no) {
		// 관리자 번호를 인수로 전달받아 승인을 완료 처리 해주는 메서드
		System.out.println("[AdminMemberDao] updateAdminAccount()");

		String sql =  "UPDATE tbl_admin_member SET "
				+ "a_m_approval = 1 "
				+ "WHERE a_m_no = ?";
		// 인수로 전달받은 id와 같은 a_m_no의 데이터에서 a_m_approval의 값을 1(승인완료)로 변경
		int result = -1; // 결과는 실패로 지정

		try {

			result = jdbcTemplate.update(sql, a_m_no);
			// update : 해당 sql로 영향받은 데이터의 개수 반환
			// 영향 받은 데이터의 개수 : 1이므로 1 반환; result = 1

		} catch (Exception e) { // 예외 발생 시
			e.printStackTrace(); // 예외 상세 내용 출력

		}

		return result; // 성공 시 1 / 실패 시 -1 반환

	}

	public int updateAdminAccount(AdminMemberVo adminMemberVo) {
		// 앞서 선언한 updateAdminAccount와 다른 오버로딩된 메서드이다.
		// AdminMemberVo객체를 매개변수로 받아 해당 계정정보를 수정하는 메서드
		System.out.println("[AdminMemberDao] updateAdminAccount()");

		String sql =  "UPDATE tbl_admin_member SET "
				+ "a_m_name = ?, "
				+ "a_m_gender = ?, "
				+ "a_m_part = ?, "
				+ "a_m_position = ?, "
				+ "a_m_mail = ?, "
				+ "a_m_phone = ?, "
				+ "a_m_mod_date = NOW() "
				+ "WHERE a_m_no = ?";
		// update SQL문 지정
		int result = -1;
		// 기본 값을 업데이트 실패로 지정
		try {

			result = jdbcTemplate.update(sql,
					adminMemberVo.getA_m_name(),
					adminMemberVo.getA_m_gender(),
					adminMemberVo.getA_m_part(),
					adminMemberVo.getA_m_position(),
					adminMemberVo.getA_m_mail(),
					adminMemberVo.getA_m_phone(),
					adminMemberVo.getA_m_no());
			// jdbcTemplate.update : 영향을 받은 행의 개수 반환 a_m_no는 기본키이므로
			// update 성공 시 result = 1
			// 전달받은 adminMemberVo 객체의 필드 값을 get메서드로 불러와 쿼리문에 파라미터로 전달해준다.

		} catch (Exception e) { // 예외 발생 시
			e.printStackTrace(); // 상셰 예외 내용 출력

		}

		return result; // 성공 시 1 / 실패 시 -1 반환

	}

	public AdminMemberVo selectAdmin(int a_m_no) {
		// 서비스에게 넘겨받은 매개변수 a_m_no : 컨트롤러에서 get 메서드로 불러온 adminMemberVo객체의 a_m_no 필드 값이다.
		// 앞선 login에서 사용된 selectAdmin(AdminmemberVo adminMemberVo) 메서드와 다른 오버로딩된 메서드이다.
		System.out.println("[AdminMemberDao] selectAdmin()");

		String sql =  "SELECT * FROM tbl_admin_member "
				+ "WHERE a_m_no = ?";
		// sql 문 지정 조건문 a_m_no를 파라미터 변수 ?로 남겨둔다.

		List<AdminMemberVo> adminMemberVos = new ArrayList<AdminMemberVo>();
		// sql 결과문을 저장하기 위한 List를 생성한다.

		try {

			adminMemberVos = jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {
				// jdbcTemplate.query : RowMapper의 타입을 똑같이 가지는 List(List<AdminMemberVo>)를 반환 타입으로 선언한다.
				// sql의 파라미터 변수에 a_m_no 값을 전달한다.
				// RowMapper : sql 결과문의 각 행을 반환타입(AdminMemberVo)객체와 매핑시켜준다.
				@Override
				public AdminMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					// mapRow: 결과 행의 각 칼럼 값을 set메서드로 반환타입(AdminMemberVo) 객체의 필드에 매핑시켜준다.
					AdminMemberVo adminMemberVo = new AdminMemberVo();
					// 값을 저장할 빈 adminMemberVo 객체 생성
					adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
					adminMemberVo.setA_m_id(rs.getString("a_m_id"));
					adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
					adminMemberVo.setA_m_name(rs.getString("a_m_name"));
					adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
					adminMemberVo.setA_m_part(rs.getString("a_m_part"));
					adminMemberVo.setA_m_position(rs.getString("a_m_position"));
					adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
					adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
					adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
					adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));
					// set 메서드로 비어있는 값을 채워
					return adminMemberVo;
					// 해당 객체 반환
				}
				// 반환 받은 객체의 모음을 리스트로 반환한다.
			}, a_m_no);

		} catch (Exception e) { // 예외 발생 기
			e.printStackTrace(); // 예외 상세내용 출력

		}

		return adminMemberVos.size() > 0 ? adminMemberVos.get(0) : null;
		// 결과 리스트가 0보다 크다면(결과가 존재한다면) 리스트의 첫번째 값 반환
		// 존재하지 않는다면 null 반환

	}

	public AdminMemberVo selectAdmin(String a_m_id, String a_m_name, String a_m_mail) {
		// 앞 선 두 selectAdmin 메서드와 다른 오버로딩된 메서드 (매개 변수 : a_m_id, a_m_name, a_m_mail)
		System.out.println("[AdminMemberDao] selectAdmin()");

		String sql =  "SELECT * FROM tbl_admin_member "
				+ "WHERE a_m_id = ? AND a_m_name = ? AND a_m_mail = ?";
		// id 이름 메일주소가 같은 데이터를 찾는 sql
		List<AdminMemberVo> adminMemberVos = new ArrayList<AdminMemberVo>();
		// sql 결과문을 받을 List 생성
		try {

			adminMemberVos = jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {
			// query : List<T> 반환 : T = AdminMemberVo
				@Override
				public AdminMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {

					AdminMemberVo adminMemberVo = new AdminMemberVo();

					adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
					adminMemberVo.setA_m_id(rs.getString("a_m_id"));
					adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
					adminMemberVo.setA_m_name(rs.getString("a_m_name"));
					adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
					adminMemberVo.setA_m_part(rs.getString("a_m_part"));
					adminMemberVo.setA_m_position(rs.getString("a_m_position"));
					adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
					adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
					adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
					adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));

					return adminMemberVo;
					// 찾은 데이터를 adminMemberVo 객체에 담아 반환
				}

			}, a_m_id, a_m_name, a_m_mail);

		} catch (Exception e) {
			e.printStackTrace();

		}

		return adminMemberVos.size() > 0 ? adminMemberVos.get(0) : null;
		// 결과 값이 존재한다면 리스트의 첫번째 값 / 존재하지 않으면 null 반환
	}

	public int updatePassword(String a_m_id, String newPassword) {
		// 매개변수로 전달받은 a_m_id 값을 가지는 데이터의 비밀번호를 새로운 비밀번호로 업데이트 하는 메서드
		System.out.println("[AdminMemberDao] updatePassword()");

		String sql =  "UPDATE tbl_admin_member SET "
				+ "a_m_pw = ?,  a_m_mod_date = NOW() "
				+ "WHERE a_m_id = ?";
		// 비밀번호를 업데이트 하는 sql문
		// 수정날짜를 현재 시점으로 변경
		int result = -1; // 기본 값을 업데이트 실패로 지정

		try {

			result = jdbcTemplate.update(sql, passwordEncoder.encode(newPassword), a_m_id);
			// 비밀 번호를 암호화하여 데이터 베이스에 업데이트

		} catch (Exception e) {
			e.printStackTrace();

		}

		return result; // 변경 성공 시 변경한 행의 수(1) / 실패 시 -1 반환
	}


}
