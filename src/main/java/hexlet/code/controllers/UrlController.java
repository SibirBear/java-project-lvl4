package hexlet.code.controllers;

import hexlet.code.model.Url;
import hexlet.code.model.query.QUrl;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class UrlController {

    public static Handler listUrls = ctx -> {
        List<Url> urlsList = new QUrl().setMaxRows(10).findList();
        ctx.attribute("urls", urlsList);
        ctx.render("urls/index.html");
    };

    public static Handler showUrl = ctx -> {
        int id = ctx.pathParamAsClass("id", Integer.class).getOrDefault(null);

        Url url = new QUrl().id.equalTo(id).findOne();

        if (url == null) {
            throw new NotFoundResponse();
        }

        ctx.attribute("url", url);
        ctx.render("urls/url.html");

    };

    public static Handler newUrl = ctx -> {
        String url = ctx.formParam("url");

        try {
            String normalizedUrl = getNormalizedUrl(url);
            if (!isNotDublicateUrl(normalizedUrl)) {
                ctx.status(422);
                ctx.sessionAttribute("flash-type", "success");
                ctx.sessionAttribute("flash", "Страница уже существует");
                ctx.redirect("urls");
            } else {
                Url newUrl = new Url(normalizedUrl);
                newUrl.save();
                ctx.sessionAttribute("flash-type", "success");
                ctx.sessionAttribute("flash", "Страница успешно добавлена");
                ctx.redirect("urls");
            }
        } catch (MalformedURLException e) {
            ctx.status(422);
            ctx.sessionAttribute("flash-type", "danger");
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.render("index.html");
        }

    };

    private static boolean isNotDublicateUrl(String url) {
        Url urlFromDB = new QUrl()
                .name.equalTo(url)
                .findOne();

        return Objects.equals(urlFromDB, null);
    }

    private static String getNormalizedUrl(String url) throws MalformedURLException {
        StringBuilder normalizedUrl = new StringBuilder();
        URL tempUrl = new URL(url);

        normalizedUrl
                .append(tempUrl.getProtocol())
                .append("://")
                .append(tempUrl.getHost());

        if (tempUrl.getPort() != -1) {
            normalizedUrl
                    .append(":")
                    .append(tempUrl.getPort());
        }

        return normalizedUrl.toString();

    }

}