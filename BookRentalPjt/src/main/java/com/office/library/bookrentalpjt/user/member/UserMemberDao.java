package com.office.library.bookrentalpjt.user.member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMemberDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate; // DB와 직접적으로 매핑하여 쿼리문을 실행시켜주는 클래스 JdbcTemplate 주입
	
	@Autowired
	PasswordEncoder passwordEncoder; // 회원가입 시 DB에 비밀번호를 암호화하여 저장하기 위해 사용하는 클래스 PasswordEncoder 주입
	
	public boolean isUserMember(String u_m_id) {
		// 매개변수로 전달받은 u_m_id로 현재 DB에 존재하는 사용자인지 확인하는 메서드
		System.out.println("[UserMemberDao] isUserMember()");
		
		String sql =  "SELECT COUNT(*) FROM tbl_user_member "
					+ "WHERE u_m_id = ?";
		
		int result = jdbcTemplate.queryForObject(sql, Integer.class, u_m_id);
		// u_m_id를 sql문의 파라미터로 전달해 해당 sql의 결과값(count 개수)을 정수로 반환한다.
		
		return result > 0 ? true : false;
		// 같은 id를 가지는 계정이 1개라도 존재한다면 true, 아니면 false 반환
		
	}

	public int insertUserAccount(UserMemberVo userMemberVo) {
		// 사용자 DB에 전달받은 사용자 객체 userMemberVo에 저장된 사용자 정보를 저장하기 위한 메서드
		System.out.println("[UserMemberDao] insertUserAccount()");
		
		String sql = "INSERT INTO tbl_user_member(u_m_id, "
											   + "u_m_pw, "
											   + "u_m_name, "
											   + "u_m_gender, "
											   + "u_m_mail, "
											   + "u_m_phone, "
											   + "u_m_reg_date, "
											   + "u_m_mod_date) VALUES(?, ?, ?, ?, ?, ?, NOW(), NOW())";
		// 삽입 쿼리문 작성 사용자 정보로 입력할 값들을 쿼리 파라미터 ?로 지정
		int result = -1; // 기본 값 : 실패
		
		try {
			
			result = jdbcTemplate.update(sql, 
											 userMemberVo.getU_m_id(), 
											 passwordEncoder.encode(userMemberVo.getU_m_pw()), 
											 userMemberVo.getU_m_name(), 
											 userMemberVo.getU_m_gender(), 
											 userMemberVo.getU_m_mail(), 
											 userMemberVo.getU_m_phone());
			// 위에서 문자열로 선언한 sql문의 파라미터에 userMemberVo객체의 필드 값을 각자의 올바른 파라미터 위치에 순서대로 전달
			// 비밀번호의 경우는 암호화 후 DB에 저장
			// 해당 쿼리문 실행 후 DB의 영향받은 행의 수 반환 : 성공 시 1 / 실패 시 0
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return result; // 성공 : 1 / 쿼리문은 실행했지만 DB 변경 x : 0 / 쿼리문 실행 X : -1
		
	}
	
	public UserMemberVo selectUser(UserMemberVo userMemberVo) {
		// id와 비밀번호를 담고 있는 userMemberVo 객체를 매개변수로 받는다.
		System.out.println("[UserMemberDao] selectUser()");
		
		String sql =  "SELECT * FROM tbl_user_member "
					+ "WHERE u_m_id = ?";
		// 해당 id를 가지는 데이터가 존재하는지 확인하는 쿼리문
		
		List<UserMemberVo> userMemberVos = new ArrayList<UserMemberVo>();
		// select 결과를 저장해둘 List 선언
		
		try {
			
			userMemberVos = jdbcTemplate.query(sql, new RowMapper<UserMemberVo>() {
				// 해당 쿼리문에 매개변수로 전달받은 userMemberVo 객체의 u_m_id값을 파라미터로 전송하여 결과값을 List로 반환
				@Override
				public UserMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					
					UserMemberVo userMemberVo = new UserMemberVo();
					
					userMemberVo.setU_m_no(rs.getInt("u_m_no"));
					userMemberVo.setU_m_id(rs.getString("u_m_id"));
					userMemberVo.setU_m_pw(rs.getString("u_m_pw"));
					userMemberVo.setU_m_name(rs.getString("u_m_name"));
					userMemberVo.setU_m_gender(rs.getString("u_m_gender"));
					userMemberVo.setU_m_mail(rs.getString("u_m_mail"));
					userMemberVo.setU_m_phone(rs.getString("u_m_phone"));
					userMemberVo.setU_m_reg_date(rs.getString("u_m_reg_date"));
					userMemberVo.setU_m_mod_date(rs.getString("u_m_mod_date"));
					
					return userMemberVo;
					
				}
				
			}, userMemberVo.getU_m_id());
			
			if (!passwordEncoder.matches(userMemberVo.getU_m_pw(), userMemberVos.get(0).getU_m_pw()))
				userMemberVos.clear();
			// 검색 결과로 나온 데이터의 비밀번호와 매개변수로 전달받은 객체 userMemberVo의 비밀번호가 동일한지 복호화를 통해 확인한다.
			// 동일하지 않다면 결과 List를 비운다.
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return userMemberVos.size() > 0 ? userMemberVos.get(0) : null;
		// 결과 List가 0보다 크다면 첫번째 값을 반환 / 아니면 null을 반환한다.
		
	}
	
	public int updateUserAccount(UserMemberVo userMemberVo) {
		// 수정정보를 담은 userMemberVo객체를 매개변수로 받아 사용자 데이터베이스를 업데이트하는 메서드
		System.out.println("[UserMemberDao] updateUserAccount()");
		
		String sql =  "UPDATE tbl_user_member SET "
					+ "u_m_name = ?, "
					+ "u_m_gender = ?, "
					+ "u_m_mail = ?, "
					+ "u_m_phone = ?, "
					+ "u_m_mod_date = NOW() "
					+ "WHERE u_m_no = ?";
		// 쿼리 파라미터에 전달받은 userMemberVo 객체에 들어있는 값을 쿼리 파라미터로 전송
		int result = -1;
		
		try {
			
			result = jdbcTemplate.update(sql, 
										 userMemberVo.getU_m_name(), 
										 userMemberVo.getU_m_gender(), 
										 userMemberVo.getU_m_mail(), 
										 userMemberVo.getU_m_phone(), 
										 userMemberVo.getU_m_no());
			// update문 실행 후 영향 받은 행의 수 반환
		} catch (Exception e) {
			e.printStackTrace();
			
		}
				
		return result;
	}
	
	public UserMemberVo selectUser(int u_m_no) {
		// 사용자 번호를 바탕으로 사용자 정보를 조회하는 쿼리문을 실행하는 메서드
		System.out.println("[UserMemberDao] selectUser()");
		
		String sql =  "SELECT * FROM tbl_user_member "
					+ "WHERE u_m_no = ?";
		
		List<UserMemberVo> userMemberVos = new ArrayList<UserMemberVo>();
		// 실행 결과를 저장할 List
		try {
			
			userMemberVos = jdbcTemplate.query(sql, new RowMapper<UserMemberVo>() {
				
				@Override
				public UserMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					
					UserMemberVo userMemberVo = new UserMemberVo();
					
					userMemberVo.setU_m_no(rs.getInt("u_m_no"));
					userMemberVo.setU_m_id(rs.getString("u_m_id"));
					userMemberVo.setU_m_pw(rs.getString("u_m_pw"));
					userMemberVo.setU_m_name(rs.getString("u_m_name"));
					userMemberVo.setU_m_gender(rs.getString("u_m_gender"));
					userMemberVo.setU_m_mail(rs.getString("u_m_mail"));
					userMemberVo.setU_m_phone(rs.getString("u_m_phone"));
					userMemberVo.setU_m_reg_date(rs.getString("u_m_reg_date"));
					userMemberVo.setU_m_mod_date(rs.getString("u_m_mod_date"));
					
					return userMemberVo;
					
				}
				
			}, u_m_no);
			// 쿼리에 u_m_no 값을 where 조건문에 파라미터 값으로 넣어 실행 후 결과를 List로 반환
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return userMemberVos.size() > 0 ? userMemberVos.get(0) : null;
		// 조회 결과가 존재한다면 해당 결과 List의 첫번째 데이터 반환, 아니면 null 반환
		
	}
	
	public UserMemberVo selectUser(String u_m_id, String u_m_name, String u_m_mail) {
		System.out.println("[UserMemberDao] selectUser()");
		
		String sql =  "SELECT * FROM tbl_user_member "
					+ "WHERE u_m_id = ? AND u_m_name = ? AND u_m_mail = ?";
	
		List<UserMemberVo> userMemberVos = new ArrayList<UserMemberVo>();
		
		try {
			
			userMemberVos = jdbcTemplate.query(sql, new RowMapper<UserMemberVo>() {
				
				@Override
				public UserMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					
					UserMemberVo userMemberVo = new UserMemberVo();
					
					userMemberVo.setU_m_no(rs.getInt("u_m_no"));
					userMemberVo.setU_m_id(rs.getString("u_m_id"));
					userMemberVo.setU_m_pw(rs.getString("u_m_pw"));
					userMemberVo.setU_m_name(rs.getString("u_m_name"));
					userMemberVo.setU_m_gender(rs.getString("u_m_gender"));
					userMemberVo.setU_m_mail(rs.getString("u_m_mail"));
					userMemberVo.setU_m_phone(rs.getString("u_m_phone"));
					userMemberVo.setU_m_reg_date(rs.getString("u_m_reg_date"));
					userMemberVo.setU_m_mod_date(rs.getString("u_m_mod_date"));
					
					return userMemberVo;
					
				}
				
			}, u_m_id, u_m_name, u_m_mail);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return userMemberVos.size() > 0 ? userMemberVos.get(0) : null;
		
	}
	
	public int updatePassword(String u_m_id, String newPassword) {
		System.out.println("[UserMemberDao] updatePassword()");
		
		String sql =  "UPDATE tbl_user_member SET "
					+ "u_m_pw = ?,  u_m_mod_date = NOW() "
					+ "WHERE u_m_id = ?";
		
		int result = -1;
		
		try {
			
			result = jdbcTemplate.update(sql, passwordEncoder.encode(newPassword), u_m_id);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		
		return result;
	}
	
	
}