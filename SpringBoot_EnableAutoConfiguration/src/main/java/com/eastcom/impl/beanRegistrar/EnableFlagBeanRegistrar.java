package com.eastcom.impl.beanRegistrar;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import com.eastcom.annotation.EnableFlag;

public class EnableFlagBeanRegistrar implements ImportBeanDefinitionRegistrar {
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		/*Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(EnableFlag.class.getName());
		String[] clStrings = (String[])map.get("strName");
		for (String cls : clStrings) {
			BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(cls);
			String beanName = builder.getBeanDefinition().getBeanClass().getSimpleName();
			registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
			System.out.println("=======>>>"+beanName);
		}*/
		/*getTypes(importingClassMetadata).forEach((type)->register(registry,type));*/
		
		Map<String, List<String>> data = getData(importingClassMetadata);
		if (data.get("strName")!=null) {
			data.get("strName").forEach((type)->register(registry,type));
		}if (data.get("value")!=null) {
			data.get("value").forEach((type)->register(registry,type));
		}
	}
	private void register(BeanDefinitionRegistry registry,String type) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(type);
		String BeanClassName = builder.getBeanDefinition().getBeanClassName().substring(
				builder.getBeanDefinition().getBeanClassName().lastIndexOf(".")+1);
		System.out.println("========BeanClassName: "+BeanClassName);
		System.out.println("========BeanName: "+builder.getBeanDefinition().getBeanClass().getSimpleName());
		registry.registerBeanDefinition(BeanClassName, builder.getBeanDefinition());
	}
	
	private Map<String , List<String>> getData(AnnotationMetadata importingClassMetadata){
		AnnotationAttributes attributes = getAttributes(importingClassMetadata);
		Map<String, List<String>> map = getAutoConfigurationMap(importingClassMetadata,attributes);
		return map;
	}
	private AnnotationAttributes getAttributes(AnnotationMetadata annotationMetadata){
		String name = EnableFlag.class.getName();
		AnnotationAttributes attributes = AnnotationAttributes
				.fromMap(annotationMetadata.getAnnotationAttributes(name, true));
		return attributes;
	}
	private Map<String , List<String>> getAutoConfigurationMap(AnnotationMetadata metadata,
			AnnotationAttributes attributes){
		Map<String , List<String>> data = new HashMap<>();
		data.put("strName",Arrays.asList(attributes.getStringArray("strName")));
		data.put("value",asList(attributes, "value"));
		return data;
	}
	private final List<String> asList(AnnotationAttributes attributes, String name) {
		String[] value = attributes.getStringArray(name);
		return Arrays.asList((value != null) ? value : new String[0]);
	}
	//无用方法，暂存
	private List<Class<?>> getTypes(AnnotationMetadata metadata) {
		Map<String, Object> attributes = metadata.getAnnotationAttributes(EnableFlag.class.getName());
		return Arrays.asList((Class<?>[])attributes.get("value"));
	}
}