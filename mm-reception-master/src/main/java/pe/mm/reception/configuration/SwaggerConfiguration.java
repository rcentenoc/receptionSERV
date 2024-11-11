package pe.mm.reception.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Configura la documentación a generar para los servicios
 *
 * @author César Calle
 * Set 02, 2019 11:39:32 PM
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    /**
     * Configura los parametros de swagger
     *
     * @return
     */
    @Bean
    public Docket mxConfigurarSwagger() {
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        aParameterBuilder.name("token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue("sigert-")
                .required(true)
                .build();
        List<springfox.documentation.service.Parameter> aParameters = new ArrayList<>();
        aParameters.add(aParameterBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2).forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("pe.mm")).build().apiInfo(mxInformacion())
                .globalOperationParameters(aParameters);
    }

    /**
     * Genera la información para mostrarse dentro de swagger respecto al api
     *
     * @return
     */
    private ApiInfo mxInformacion() {
        Contact loContact = new Contact("Mentor Monitor Pro", "http://www.mentormonitorpro.com",
                "mcalle@ulasalle.edu.pe");

        return new ApiInfo("REST API de Mentor Monitor",
                "El api excompone funcionalidades para poder comunicarse con la plataforma",
                "1.0", "urn:tos", loContact, "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>());
    }




}
