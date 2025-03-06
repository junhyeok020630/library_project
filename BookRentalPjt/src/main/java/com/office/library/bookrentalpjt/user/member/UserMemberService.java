package com.office.library.bookrentalpjt.user.member;

import java.security.SecureRandom;
import java.util.Date;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class UserMemberService {
	
	final static public int USER_ACCOUNT_ALREADY_EXIST = 0; // 계정이 이미 존재할 때 사용할 상수값 0
	final static public int USER_ACCOUNT_CREATE_SUCCESS = 1; // 회원가입 성공 시 사용할 상수값 1
	final static public int USER_ACCOUNT_CREATE_FAIL = -1; // 회원가입 실패 시 사용할 상수값 -1
	
	@Autowired
	UserMemberDao userMemberDao; // 사용자 DB 관련 메서드를 저장하고 있는 userMemberDao 객체 주입

	@Autowired
	JavaMailSenderImpl javaMailSenderImpl; // 비밀번호 찾기 시 사용자에게 메일을 전송하기 위해 JavaMailSenderImpl 인터페이스 주입
	
	public int createAccountConfirm(UserMemberVo userMemberVo) {
		// 회원가입 요청 확인 처리 메서드
		// 폼에서 작성한 사용자 정보를 담고 있는 userMemberVo 객체를 매개변수로 받는다.
		System.out.println("[UserMemberService] createAccountConfirm()");
		
		boolean isMember = userMemberDao.isUserMember(userMemberVo.getU_m_id());
		// 사용자 정보를 담고 있는 객체의 u_m_id 값을 Dao의 isUserMember 메서드에 전송한다.
		// isUserMember() : 현재 사용자 데이터베이스에 전달받은 id를 가지는 사용자가 존재하는 지 확인 후 boolean를 반환한다.
		
		if (!isMember) { // 사용자가 존재하지 않는다면
			int result = userMemberDao.insertUserAccount(userMemberVo);
			// Dao의 insertUserAccount () 메서드에 사용자 정보를 담은 객체를 인수로 전달한다.
			// insertUserAccount() : 사용자 데이터베이스에 전달받은 객체의 사용자 정보를 저장하는 메서드
			// 성공 시 1 / 실패 시 -1 or 0 반환
			
			if (result > 0) // 회원가입 성공 시
				return USER_ACCOUNT_CREATE_SUCCESS; // 1 반환
			
			else // -1 or 0 : 회원가입 쿼리문이 실행되었지만 DB 변경 X 혹은 쿼리문 실행 X
				return USER_ACCOUNT_CREATE_FAIL; // -1 반환
			
		} else { // 이미 존재하는 사용자
			return USER_ACCOUNT_ALREADY_EXIST; // 0 반환
			
		}
		
	}
	
	public UserMemberVo loginConfirm(UserMemberVo userMemberVo) {
		// 로그인 폼에서 작성한 id와 비밀번호를 담고 있는 userMemberVo 객체를 매개변수로 전달 받는다.
		System.out.println("[UserMemberService] loginConfirm()");
		
		UserMemberVo loginedUserMemberVo = userMemberDao.selectUser(userMemberVo);
		// id와 비밀번호를 들고 있는 객체를 Dao의 selectUser() 메서드에 매개변수로 전달
		// selectUser() : DB에서 같은 Id를 가지는 사용자가 있는지 확인 후 DB에 저장되어 있는 암호화된 pw를 복호화하여 입력받은 비밀번호와 동일한지 확인하여
		// 해당 사용자 정보를 담은 UserMemberVo 타입의 객체를 반환한다.
		
		if (loginedUserMemberVo != null) // 반환 받은 객체가 존재하면
			System.out.println("[UserMemberService] USER MEMBER LOGIN SUCCESS!!"); // 로그인 성공
		else // 반환받은 객체가 null이라면
			System.out.println("[UserMemberService] USER MEMBER LOGIN FAIL!!"); // 로그인 실패
		
		return loginedUserMemberVo; // 컨트롤러에 반환받은 사용자 정보를 담은 객체를 넘겨준다.
		
	}
	
	public int modifyAccountConfirm(UserMemberVo userMemberVo) {
		// 계정 수정 확인 처리 메서드
		// 계정 수정 정보를 담은 userMemberVo 객체를 매개변수로 받는다.
		System.out.println("[UserMemberService] modifyAccountConfirm()");
		
		return userMemberDao.updateUserAccount(userMemberVo);
		// Dao의 updateuserAccount() 메서드에 userMemberVo를 매개변수로 전달한다.
		// updateuserAccount() : 전달 받은 객체의 u_m_no와 동일한 값을 가지는 데이터를 전송 받은 객체의 필드 값을 토대로 변경한다.
		// update 실행 후 영향을 받은 행의 수 반환
		
	}
	
	public UserMemberVo getLoginedUserMemberVo(int u_m_no) {
		// 매개 변수로 계정 수정 폼에서 전달받은 객체의 u_m_no값을 가져온다.
		System.out.println("[UserMemberService] getLoginedUserMemberVo()");
		
		return userMemberDao.selectUser(u_m_no);
		// 매개변수로 받은 값을 Dao의 selectUser()의 매개변수로 전달한다.
		// selectUser(int u_m_no) : 번호를 where 조건문에 넣어 select문을 실행하여 나오는 결과 데이터를 UserMemberVo객체에 담아 반환한다.
		
	}
	
	public int findPasswordConfirm(UserMemberVo userMemberVo) {
		// 비밀번호 찾기 요청 처리 메서드
		// id, 이름, 메일 정보를 담고 있는 userMemberVo
		System.out.println("[UserMemberService] findPasswordConfirm()");
		
		UserMemberVo selectedUserMemberVo = userMemberDao.selectUser(userMemberVo.getU_m_id(),
																	 userMemberVo.getU_m_name(), 
																	 userMemberVo.getU_m_mail());
		// id와 이름, mail을 Dao의 selectUser()메서드의 매개변수로 전달
		// selectUser(String u_m_id, String u_m_name, String u_m_mail) : id 이름 메일을 조건문에 넣어 동일한 사용자를 찾아 반환하는 메서드
		int result = 0; // 기본 결과 값 실패로 지정
		
		if (selectedUserMemberVo != null) {
			// 조회 결과가 존재한다면
			
			String newPassword = createNewPassword(); // 새로운 비밀 번호 생성
			// createNewPassword() : 특정 문자를 랜덤 조합해 생성

			result = userMemberDao.updatePassword(userMemberVo.getU_m_id(), newPassword);
			// id와 새로운 비밀번호를 Dao의 updatePassword의 매개변수로 전달
			// id로 데이터를 찾아 해당 데이터의 비밀번호를 newPassword를 암호화하여 저장
			// 저장 완료 시 1 반횐 / 비밀번호 미 변경 시 0 반환 / 실패 시 -1 반환

			if (result > 0)
				sendNewPasswordByMail(userMemberVo.getU_m_mail(), newPassword);
			// DB에 새로운 비밀번호를 저장했다면, 사용자 이메일에 새로운 비밀번호 전송
			
		} 
		
		return result; // 비밀번호 찾기 완료 시 1 반횐 / DB의 비밀번호 미 변경 시 0 반환 / 실패 시 -1 반환
		
	}
	
	private String createNewPassword() {
		// 새 비밀번호 생성 메서드
		System.out.println("[AdminMemberService] createNewPassword()");
		
		char[] chars = new char[] {
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 
				'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 
				'u', 'v', 'w', 'x', 'y', 'z'
				};

		StringBuffer stringBuffer = new StringBuffer();
		SecureRandom secureRandom = new SecureRandom();
		// 문자 배열을 랜더으로 조합하여 새로운 비밀번호 생성
		secureRandom.setSeed(new Date().getTime());
		
		int index = 0;
		int length = chars.length;
		for (int i = 0; i < 8; i++) {
			index = secureRandom.nextInt(length);
		
			if (index % 2 == 0) 
				stringBuffer.append(String.valueOf(chars[index]).toUpperCase());
			else
				stringBuffer.append(String.valueOf(chars[index]).toLowerCase());
		
		}
		
		System.out.println("[AdminMemberService] NEW PASSWORD: " + stringBuffer.toString());
		
		return stringBuffer.toString();
		
	}
	
	private void sendNewPasswordByMail(String toMailAddr, String newPassword) {
		// 새로운 비밀번호를 메일로 전송하는 메서드
		// toMailAddr에 사용자의 메일이 들어가야 하지만 ( getU_m_mail 메서드로 사용자의 메일 불러오기 )
		// 연습이므로 수신 가능한 메일 주소로 지정
		System.out.println("[AdminMemberService] sendNewPasswordByMail()");
		
		final MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
			// MImeMessagePreparator를 구현한 익명 클래스 생성
			// prepare()에 받는 메일 주소, 제목, 본문 데이터를 설정한다.
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				mimeMessageHelper.setTo("skylove2424@gmail.com");	// 수신 가능한 개인 메일 주소
//				mimeMessageHelper.setTo(toMailAddr);
				mimeMessageHelper.setSubject("[한국 도서관] 새 비밀번호 안내입니다.");
				mimeMessageHelper.setText("새 비밀번호 : " + newPassword, true);
				
			}
			
		};
		javaMailSenderImpl.send(mimeMessagePreparator);
		// prepare로 지정한 메일의 내용을 전송함
		
	}
	
}