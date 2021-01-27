package com.example.application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.frontend.FrontendTools;
import com.vaadin.flow.server.frontend.FrontendUtils;
import com.vaadin.flow.theme.Theme;

import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.vaadin.artur.exampledata.NodeScriptInterface;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "compile-ts-util-during-build")
@PWA(name = "compile-ts-util-during-build", shortName = "compile-ts-util-during-build", offlineResources = {
        "images/logo.png" })
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

    private static void waitForFile(String fileNameInProject, int maxTime) {
        File f = new File(fileNameInProject);
        if (!f.exists()) {
            LoggerFactory.getLogger(Application.class)
                    .info("Waiting for " + fileNameInProject + " to become available");
        }
        while (!f.exists() && maxTime-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
            LoggerFactory.getLogger(Application.class)
                    .debug("Waiting for " + fileNameInProject + " to become available");
        }
    }

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);

        waitForFile("util.js", 300);

        FrontendTools tools = new FrontendTools("", () -> FrontendUtils.getVaadinHomeDirectory().getAbsolutePath());
        String node = tools.getNodeExecutable();
        List<String> command = new ArrayList<>();
        command.add(node);
        command.add("-r");
        command.add("esm");
        command.add("util.js");

        ProcessBuilder builder = FrontendUtils.createProcessBuilder(command);
        Process nodeProcess = builder.start();
        String output = IOUtils.toString(nodeProcess.getInputStream(), "UTF-8");
        System.out.println("Util says: " + output);
    }

}
