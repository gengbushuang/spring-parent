package com.annotation.imp.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.Set;

//自定义逻辑要返回需要的导入组件
public class MyImportSelector implements ImportSelector {

    //AnnotationMetadata 当前标注的@Import类
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Set<String> annotationTypes = importingClassMetadata.getAnnotationTypes();
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes("org.springframework.context.annotation.Import");

        return new String[]{"com.annotation.model.Black","com.annotation.model.Blue","com.annotation.model.Red"};
    }
}
