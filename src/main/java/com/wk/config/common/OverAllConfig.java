package com.wk.config.common;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

/**
 * @author wkgui
 * @date 2017/12/6
 */
public class OverAllConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SpringRootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{ServletConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    /**
     * 使用servlet3.0支持的上传方式
     * @param registration
     */
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        String path = OverAllConfig.class.getResource("/").getPath();
        path = path.substring(0, path.length()-"/WEB-INF/classes/".length());
        registration.setMultipartConfig(
                new MultipartConfigElement(path+"/WEB-INF/upload/", 32*1024*1024, 32*1024*1024, 0));
    }

}
