package com.office.library.bookrentalpjt.config;

import com.office.library.bookrentalpjt.admin.member.AdminMemberLoginInterceptor;
import com.office.library.bookrentalpjt.user.member.UserMemberLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AdminMemberLoginInterceptor adminMemberLoginInterceptor;
    @Autowired
    private UserMemberLoginInterceptor userMemberLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

            registry.addInterceptor(adminMemberLoginInterceptor)
                    .addPathPatterns("/admin/member/**")
                    .addPathPatterns("/book/admin/returnBookConfirm")
                    .excludePathPatterns("/admin/member/bookDetail")
                    .excludePathPatterns("/admin/member/createAccountForm")
                    .excludePathPatterns("/admin/member/createAccountConfirm")
                    .excludePathPatterns("/admin/member/loginForm")
                    .excludePathPatterns("/admin/member/loginConfirm")
                    .excludePathPatterns("/admin/member/findPasswordForm")
                    .excludePathPatterns("/admin/member/findPasswordConfirm")
            ;


            registry.addInterceptor(userMemberLoginInterceptor)
                    .addPathPatterns("/book/user/rentalBookConfirm")
                    .addPathPatterns("/book/user/enterBookshelf")
                    .addPathPatterns("/book/user/listupRentalBookHistory")
                    .addPathPatterns("/book/user/requestHopeBookConfirm")
                    .addPathPatterns("/book/user/listupRequestHopeBook")
                    .addPathPatterns("/user/member/**")
                    .excludePathPatterns("/user/member/createAccountForm")
                    .excludePathPatterns("/user/member/createAccountConfirm")
                    .excludePathPatterns("/user/member/loginForm")
                    .excludePathPatterns("/user/member/loginConfirm")
                    .excludePathPatterns("/user/member/findPasswordForm")
                    .excludePathPatterns("/user/member/findPasswordConfirm");


    }
}
