package com.office.library.bookrentalpjt.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

public class LibraryBeanNameGenerator implements BeanNameGenerator {
	// 빈 이름 생성기 - 메인함수에 ComponentScan 어노테이션으로 프로젝트에 등록하여 해당 빈 이름 생성기로 빈 등록을 하겠다 선언

	@Override
	public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		
		System.out.println(definition.getBeanClassName());
		
		return definition.getBeanClassName();
		
	}

}
