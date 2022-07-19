package hexlet.code;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;

public class App {

    public static void main(String[] args) {
       Javalin javalin = getApp();
       javalin.start(getPort());
    }

    public static Javalin getApp() {
        Javalin javalin= Javalin.create(JavalinConfig::enableDevLogging);
        addRoutes(javalin);
        return javalin;
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "8091");
        return Integer.parseInt(port);
    }

    private static void addRoutes(Javalin javalin) {
        javalin.get("/", ctx -> ctx.result("Hello, world!"));
    }

}
