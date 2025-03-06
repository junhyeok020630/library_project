package com.office.library.bookrentalpjt.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "home"; // application properties에서 설정한 값에 따라 html이 아닌 jsp로
    }

    @GetMapping("/book/admin/deleteBookForm")
    public String deleteBookForm() {
        return "admin/book/delete_book_ok";
    }
}
