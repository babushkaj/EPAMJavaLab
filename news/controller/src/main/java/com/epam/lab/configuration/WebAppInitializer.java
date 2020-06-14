package com.epam.lab.configuration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class WebAppInitializer implements WebApplicationInitializer {
    private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
    private static final String PRODUCTION_PROFILE = "prod";

    @Override
    public void onStartup(ServletContext servletContext) {

        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();
        servletContext.setInitParameter(SPRING_PROFILES_ACTIVE, PRODUCTION_PROFILE);
        rootContext.register(RepositoryConfig.class, ServiceConfig.class, ControllerConfig.class);
        rootContext.setServletContext(servletContext);
        rootContext.refresh();

        ServletRegistration.Dynamic dispatcher =
                servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}

//public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
//    private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
//    private static final String PRODUCTION_PROFILE = "prod";
//
//    @Override
//    public void onStartup(ServletContext servletContext) {
//
//        AnnotationConfigWebApplicationContext rootContext =
//                new AnnotationConfigWebApplicationContext();
//        servletContext.setInitParameter(SPRING_PROFILES_ACTIVE, PRODUCTION_PROFILE);
//        rootContext.register(RepositoryConfig.class, ServiceConfig.class, ControllerConfig.class);
//        rootContext.setServletContext(servletContext);
//        rootContext.refresh();
//
//        ServletRegistration.Dynamic dispatcher =
//                servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext));
//        dispatcher.setLoadOnStartup(1);
//        dispatcher.addMapping("/");
//    }
//
//    @Override
//    protected String[] getServletMappings() {
//        return null;
//    }
//
//    @Override
//    protected Class<?>[] getRootConfigClasses() {
//        return new Class<?>[]{};
//    }
//
//    @Override
//    protected Class<?>[] getServletConfigClasses() {
//        return null;
//    }
//
//    @Override
//    protected Filter[] getServletFilters() {
//        return new Filter[]{};
//    }
//}

