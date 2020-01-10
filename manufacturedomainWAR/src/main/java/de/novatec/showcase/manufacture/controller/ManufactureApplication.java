package de.novatec.showcase.manufacture.controller;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@ApplicationPath(value = "")
@OpenAPIDefinition(info = @Info(title = "The manufacture domains api.", version ="1.0"))
public class ManufactureApplication extends Application {

}
