package com.example;

import com.example.handler.DispatcherServlet;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;

public class DemoApplication {
    private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) throws ServletException {
        log.info("Start undertow...");

        DeploymentInfo deploymentInfo = Servlets.deployment()
                .setClassLoader(DemoApplication.class.getClassLoader())
                .setContextPath("/")
                .setDeploymentName("web.war")
                .addServlets(Servlets.servlet("DispatcherServlet", DispatcherServlet.class).addMapping("/"));

        ServletContainer servletContainer = Servlets.defaultContainer();
        DeploymentManager deploymentManager = servletContainer.addDeployment(deploymentInfo);
        deploymentManager.deploy();

        Undertow undertow = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(Handlers.path(deploymentManager.start()))
                .build();

        undertow.start();

        undertow.getListenerInfo().forEach(info -> {
            log.info("Started with {}.", info.getAddress());
        });
    }
}
