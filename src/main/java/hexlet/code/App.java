package hexlet.code;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class App {

    private static final String CTX = "ctx";

    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(getPort());
    }

    private static Javalin getApp() {
        Javalin app = Javalin.create(config -> {
            config.enableDevLogging();
            config.enableWebjars();
            JavalinThymeleaf.configure(getTemplateEngine());
        });

        addRoutes(app);

        app.before(ctx -> ctx.attribute(CTX, ctx));

        return app;
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "3000");
        return Integer.parseInt(port);
    }

    private static void addRoutes(Javalin app) {
        app.get("/", ctx -> ctx.render("index.html"));
    }

    private static TemplateEngine getTemplateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");

        templateEngine.addTemplateResolver(templateResolver);
        templateEngine.addDialect(new LayoutDialect());
        templateEngine.addDialect(new Java8TimeDialect());

        return templateEngine;

    }

}
