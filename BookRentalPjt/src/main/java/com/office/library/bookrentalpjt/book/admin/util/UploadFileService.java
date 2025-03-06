package com.office.library.bookrentalpjt.book.admin.util;

import java.io.File;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService {

	public String upload(MultipartFile file) {
		// MultipartFile 타입의 파일 업로드
		boolean result = false;
		
		// File 저장
		String fileOriName = file.getOriginalFilename();
		// 관리자가 업로드한 원본 파일의 이름을 가져온다.
		String fileExtension = 
				fileOriName.substring(fileOriName.lastIndexOf("."), fileOriName.length());
		// 관리자가 업로드한 원본 파일의 확장자를 가져온다.
		String uploadDir = "C:\\library\\upload\\";
		// 서버에서 파일이 저장되는 위치 정의
		
		UUID uuid = UUID.randomUUID();
		String uniqueName = uuid.toString().replaceAll("-", "");
		// 자바에서 제공하는 UUID 클래스를 통해 유일한 식별자를 얻는다. -를 모두 제거하여 uniqueName을 얻는다.
		// 파일이름의 중복을 막기위해 uniqueName으로 변경하여 서버에 파일을 저장한다.
		
		File saveFile = new File(uploadDir + "\\" + uniqueName + fileExtension);
		// 서버에 저장되는 파일 객체를 생성한다. 저장경로(uploadDir), 파일이름(uniqueName), 확장자(fileExtension) 이용
		
		if (!saveFile.exists()) // 서버에 파일이 저장되는 디렉터리가 존재하지 않는다면
			saveFile.mkdirs(); // 새 디렉터리를 생성
		
		try { // 서버에 파일을 저장하고 result 를 true로 변경한다.
			file.transferTo(saveFile);
			result = true;
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		if (result) { // 결과가 true라면
			System.out.println("[UploadFileService] FILE UPLOAD SUCCESS!!");
			return uniqueName + fileExtension; // 파일이름에 확장자를 붙여 반환한다.
			
		} else {
			System.out.println("[UploadFileService] FILE UPLOAD FAIL!!");
			return null; // null을 반환한다.
			
		}
		
		
	}
	
}
