package hexlet.code.controllers;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.model.query.QUrl;
import hexlet.code.model.query.QUrlCheck;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UrlController {

    private static final int MAX_ROWS = 10;

    public static Handler listUrls = ctx -> {
        List<Url> urlsList = new QUrl().setMaxRows(MAX_ROWS).findList();

        Map<Integer, UrlCheck> urlCheckList = new QUrlCheck()
                .url.id.asMapKey().orderBy().id.asc().findMap();

        ctx.attribute("urls", urlsList);
        ctx.attribute("urlCheckList", urlCheckList);
        ctx.render("urls/index.html");
    };

    public static Handler showUrl = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);

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
            ctx.sessionAttribute("flash-type", "success");

            if (!isNotDublicateUrl(normalizedUrl)) {
                ctx.sessionAttribute("flash", "Страница уже существует");
                ctx.redirect("urls");
            } else {
                Url newUrl = new Url(normalizedUrl);
                newUrl.save();
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

    public static Handler checkUrl = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);

        Url url = new QUrl().id.equalTo(id).findOne();

        if (url == null) {
            throw new NotFoundResponse();
        }

        HttpResponse<String> response;

        try {
            response = Unirest.get(url.getName()).asString();

        } catch (UnirestException e) {
            ctx.sessionAttribute("flash-type", "danger");
            ctx.sessionAttribute("flash", "Страница не существует");
            ctx.redirect("/urls/" + id);
            return;
        }

        UrlCheck urlCheck = new UrlCheck();
        urlCheck.setStatusCode(response.getStatus());
        urlCheck.setUrl(url);
        urlCheck.save();

        ctx.sessionAttribute("flash-type", "success");
        ctx.sessionAttribute("flash", "Страница успешно проверена");
        ctx.redirect("/urls/" + id);

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
