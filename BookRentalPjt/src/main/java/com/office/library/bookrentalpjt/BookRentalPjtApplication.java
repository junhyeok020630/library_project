package com.office.library.bookrentalpjt;

import com.office.library.bookrentalpjt.config.LibraryBeanNameGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(nameGenerator = LibraryBeanNameGenerator.class)
public class BookRentalPjtApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookRentalPjtApplication.class, args);
    }
// superAdmin 비밀 번호 : GSEtrI6p
}
