package com.office.library.bookrentalpjt.admin.member;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

@Service // 해당 클래스를 Service로 빈 등록 해둔다; Service : 실질적인 로직과 관련된 메서드를 저장해둠
@RequiredArgsConstructor // lombok에서 생성자 주입을 위해 제공하는 어노테이션
public class AdminMemberService {

	final static public int ADMIN_ACCOUNT_ALREADY_EXIST = 0; // 계정이 이미 존재할 때의 상수값
	final static public int ADMIN_ACCOUNT_CREATE_SUCCESS = 1; // 회원 가입 성공할 때 상수값
	final static public int ADMIN_ACCOUNT_CREATE_FAIL = -1; // 회원 가입 실패 시 상수값
	

	private final AdminMemberDao adminMemberDao; // Dao의 데이터베이스를 직접적으로 다루는 메서드에 접근하기 위해 주입
	private final JavaMailSenderImpl javaMailSenderImpl;
	
	public int createAccountConfirm(AdminMemberVo adminMemberVo) { // 회원가입 확인 메서드
		System.out.println("[AdminMemberService] createAccountConfirm()");
		
		boolean isMember = adminMemberDao.isAdminMember(adminMemberVo.getA_m_id());
		// 이미 존재하는 관리자인지 확인하는 boolean 값
		// adminMemberDao의 isAdminMember 메서드 사용 - id 값을 받아와 데이터베이스에서 조건에 해당하는 값이 존재하는지 select
		
		if (!isMember) { // 존재하지 않는 관리자라면
			int result = adminMemberDao.insertAdminAccount(adminMemberVo);
			// adminMemberDao의 insertAdminAccount 메서드 사용 - form 태그와 매핑된 Vo 객체를 인수로 불러와 실제 데이터베이스에 insert
			// 성공 시 1 / 실패 시 -1
			if (result > 0)
				return ADMIN_ACCOUNT_CREATE_SUCCESS; // 성공 시 성공 상수값(1) 반환
			
			else
				return ADMIN_ACCOUNT_CREATE_FAIL; // 실패 상수값(-1) 반환
			
		} else { // 이미 존재하는 관리자라면
			return ADMIN_ACCOUNT_ALREADY_EXIST; // 관리자 계정이 이미 존재할때의 상수값(0) 반환
			
		}
		
	}

	public AdminMemberVo loginConfirm(AdminMemberVo adminMemberVo) { // 로그인 성공을 확인하는 메서드
		System.out.println("[AdminMemberService] loginConfirm()");

		AdminMemberVo loginedAdminMemberVo = adminMemberDao.selectAdmin(adminMemberVo);
		// 로그인 폼에서 받은 아이디와 비밀번호를 담은 Vo객체(adminMemberVo)
		// adminMemberDao의 selectAdmin(AdminMemberVo adminMemberVo) 메서드 사용 - select문으로 해당 Vo객체를 set 메서드로 채워서 반환

		if (loginedAdminMemberVo != null) // 반환받은 객체가 null이 아닐 경우
			System.out.println("[AdminMemberService] ADMIN MEMBER LOGIN SUCCESS!!"); // 로그인 성공
		else // 반환받은 객체가 null
			System.out.println("[AdminMemberService] ADMIN MEMBER LOGIN FAIL!!"); // 로그인 실패

		return loginedAdminMemberVo; //로그인 정보가 결정된 Vo객체 (로그인 성공 시 관리자 정보를 담고 있는 Vo, 실패 시 null)

	}

	public List<AdminMemberVo> listupAdmin() { // dao의 전체 행 출력 메서드 결과물을 컨트롤러에게 반환해주는 메서드
		System.out.println("[AdminMemberService] listupAdmin()");

		return adminMemberDao.selectAdmins(); // dao 객체의 selectAdmins 메서드 사용 - 관리자 데이터베이스에 존재하는 전체 데이터를 반환하는 메서드

	}

	public void setAdminApproval(int a_m_no) { // dao의 관리자 승인 메서드 결과물을 컨트롤러에게 반환해 주는 메서드
		System.out.println("[AdminMemberService] setAdminApproval()");

		int result = adminMemberDao.updateAdminAccount(a_m_no);
		// dao 객체의 updateAdminAccount 메서드 사용 - a_m_no를 인수로 받아 해당하는 관리자의 승인을 1로 업데이트하여 승인완료 상태로 변환한다.
		// 메서드 성공 시 result로 1을 반환 받는다.

	}

	public int modifyAccountConfirm(AdminMemberVo adminMemberVo) {
		// 컨틀롤러에서 modify_account_form과 매핑되어 수정 값이 저장되어있는 객체 adminMemberVo를 매개변수로 받는다.
		System.out.println("[AdminMemberService] modifyAccountConfirm()");

		return adminMemberDao.updateAdminAccount(adminMemberVo);
		// 해당 객체를 데이터베이스와 직접적으로 연결시켜주는 Dao의 updateAdminAccount 메서드의 매개변수로 넘겨준다.
		// updateAdminAccount : 해당 객체의 필드 값을 파라미터로 불러와 SQL문에 넣어서 데이터베이스 update 실행

	}

	public AdminMemberVo getLoginedAdminMemberVo(int a_m_no) {
		// 매개변수로 a_m_no로 요청받는다.
		// 컨트롤러의 modifyAccountConfirm에서 변경된 계정정보를 담은 객체를 불러오기 위해 사용하는 메서드로
		// 매개변수에 adminMemberVo객체의 a_m_no의 필드 값을 get메서드로 불러와 요청한다.

		System.out.println("[AdminMemberService] getLoginedAdminMemberVo()");

		return adminMemberDao.selectAdmin(a_m_no);
		// 해당 정수 값(adminMemberVo 객체의 a_m_no 필드 값을 Dao의 selectAdmin 메서드에 매개변수로 전달한다.
		// selectAdmin(int a_m_no) : 전달받은 a_m_no 값을 쿼리문의 조건문에 넣어 나온 select 값을 객체로 반환한다.
		// select 성공 시 해당 객체 반환, 실패 시 null 반환

	}

	public int findPasswordConfirm(AdminMemberVo adminMemberVo) {
		// adminMemberVo(a_m_id, a_m_name, a_m_mail 필드를 가진) 객체를 매개변수로 받는다.
		System.out.println("[AdminMemberService] findPasswordConfirm()");

		AdminMemberVo selectedAdminMemberVo = adminMemberDao.selectAdmin(adminMemberVo.getA_m_id(),
				adminMemberVo.getA_m_name(),
				adminMemberVo.getA_m_mail());
		// 매개변수로 전달받은 adminMemberVo객체의 필드를 get메서드로 불러와 Dao의 selectAdmin(String a_m_id, String a_m_name, String a_m_mail)에 전송한다.
		// selectAdmin(String a_m_id, String a_m_name, String a_m_mail) : 해당 매개변수 값을 포함하는 데이터를 AdminMemberVo 객체로 반환해주는 메서드

		int result = 0; // 기본을 실패 값으로 지정

		if (selectedAdminMemberVo != null) { // 조회한 데이터가 존재한다면

			String newPassword = createNewPassword();
			// 새로운 비밀번호 생성
			// createNewPassword() : 특정 문자들을 랜덤으로 조합하여 8글자의 비밀번호 생성
			result = adminMemberDao.updatePassword(adminMemberVo.getA_m_id(), newPassword);
			// result : Dao의 updatePassword 메서드의 jdbcTemplate.update 값을 받아 저장
			if (result > 0) // 비밀번호 업데이트에 성공했다면
				sendNewPasswordByMail(adminMemberVo.getA_m_mail(), newPassword);
				// 해당 메일로 새 비밀번호 이메일 전송
				// 이때 메일을 발송하는 주소가 properties에서 지정한 gmail계정이다.
				// sendNewPasswordByMail : 지정한 주소로 메일을 보내는 메서드
		}

		return result; // 실패 시 0 / 성공 시 1 반환

	}

	private String createNewPassword() {
		System.out.println("[AdminMemberService] createNewPassword()");

		char[] chars = new char[] {
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
				'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
				'u', 'v', 'w', 'x', 'y', 'z'
		};

		StringBuffer stringBuffer = new StringBuffer();
		SecureRandom secureRandom = new SecureRandom();
		// random보다 강력한 난수 생성
		secureRandom.setSeed(new Date().getTime()); // 현재 시간을 seed로 정하여 해당 값을 기준으로 난수 생성

		int index = 0;
		int length = chars.length;
		for (int i = 0; i < 8; i++) {
			index = secureRandom.nextInt(length);

			if (index % 2 == 0)
				stringBuffer.append(String.valueOf(chars[index]).toUpperCase());
			else
				stringBuffer.append(String.valueOf(chars[index]).toLowerCase());

		}
		// 난수로 비밀번호를 생성하여 stringBuffer에 추가한다.

		System.out.println("[AdminMemberService] NEW PASSWORD: " + stringBuffer.toString());

		// 새로운 비밀번호 출력
		return stringBuffer.toString(); // 새로운 비밀번호 반환

	}

	private void sendNewPasswordByMail(String toMailAddr, String newPassword) {
		// toMailAddr : adminMemberVo 객체의 getA_m_mail 메서드로 메일 주소를 받아와 해당 메일 주소에 메일을 전송 해야하지만,
		// 해당 연습 코드는 임의로 지정한 메일 주소 이므로 특정 메일 주소(저자의 메일 주소)에 전송해줌
		System.out.println("[AdminMemberService] sendNewPasswordByMail()");

		final MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
			// MImeMessagePreparator를 구현한 익명 클래스 생성
			// prepare()에 받는 메일 주소, 제목, 본문 데이터를 설정한다.
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				mimeMessageHelper.setTo("skylove2424@gmail.com"); // 받는 메일 주소
//				mimeMessageHelper.setTo(toMailAddr);
				mimeMessageHelper.setSubject("[한국 도서관] 새 비밀번호 안내입니다."); // 메일 제목
				mimeMessageHelper.setText("새 비밀번호 : " + newPassword, true); // 메일 내용

			}

		};
		javaMailSenderImpl.send(mimeMessagePreparator); // prepare()로 지정한 내용을 담은 객체를 메일에 담아 전송한다.

	}

}
