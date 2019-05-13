package com.annotation.scan;

import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

public class MyTypeFilter implements TypeFilter {
    //MetadataReader 当前真正扫描的类
    //MetadataReaderFactory
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        //注解信息
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        //类信息
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        //资源信息
        Resource resource = metadataReader.getResource();
        String className = classMetadata.getClassName();
        System.out.println("className-->" + className);

        return className.contains("Co");
    }
}
