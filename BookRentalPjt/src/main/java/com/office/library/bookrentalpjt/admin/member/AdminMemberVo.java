package com.office.library.bookrentalpjt.admin.member;

import lombok.Getter;
import lombok.Setter;

@Getter // lombok에서 지원하는 어노테이션; 자동으로 getter 메서드를 추가해준다
@Setter // lombok에서 지원하는 어노테이션; 자동으로 setter 메서드를 추가해준다
public class AdminMemberVo { // 관리자의 데이터에 대한 요청을 주고받을 때 사용하는 객체

	// 해당 객체의 필드와 create_account_form의 input 태그에서 name 필드가 자동으로 매핑된다.
	// 스프링 내부적으로 해당 객체의 setter 메서드를 호출해 값을 지정한다. - setter 메서드는 lombok으로 선언하였다.

	private int a_m_no;			// 관리자 번호
	private int a_m_approval;		// 최고 관리자 승인 여부
	private String a_m_id;			// 관리자 아이디
	private String a_m_pw;			// 관리자 비밀번호
	private String a_m_name;		// 관리자 이름
	private String a_m_gender;		// 관리자 성별 구분
	private String a_m_part;		// 관리자 근무 부서
	private String a_m_position;		// 관리자 업무
	private String a_m_mail;		// 관리자 메일
	private String a_m_phone;		// 관리자 연락처
	private String a_m_reg_date;		// 관리자 등록일
	private String a_m_mod_date;		// 관리자 수정일

	
}
