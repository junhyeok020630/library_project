package com.office.library.bookrentalpjt.user.member;

import lombok.Getter;
import lombok.Setter;

@Getter // Lombok에서 제공하는 어노테이션 : Getter 메서드 자동 추가
@Setter // Lombok에서 제공하는 어노테이션 : Setter 메서드 자동 추가
// 사용자 정보를 저장해둘 Vo 객체
public class UserMemberVo {
	
	int u_m_no;
	String u_m_id;
	String u_m_pw;
	String u_m_name;
	String u_m_gender;
	String u_m_mail;
	String u_m_phone;
	String u_m_reg_date;
	String u_m_mod_date;
	
}
