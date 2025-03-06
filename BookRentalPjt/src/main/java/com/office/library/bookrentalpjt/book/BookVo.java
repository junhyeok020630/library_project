package com.office.library.bookrentalpjt.book;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 책 상세 내용을 담은 Vo 객체
public class BookVo {

	private int b_no;
	private String b_thumbnail;
	private String b_name;
	private String b_author;
	private String b_publisher;
	private String b_publish_year;
	private String b_isbn;
	private String b_call_number;
	private int b_rantal_able;
	private String b_reg_date;
	private String b_mod_date;

	
}
