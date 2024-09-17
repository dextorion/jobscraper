package com.lifetech.job.client;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.Function;

@Component
public class LinkedinDocumentSupplier implements Function<Integer, Document> {

    private static final Logger log = LoggerFactory.getLogger(LinkedinDocumentSupplier.class);

    private final String url;
    private final String keywords;
    private final String geoId;
    private final String places;

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.5112.79 Safari/537.36";

    public LinkedinDocumentSupplier(
            @Value("${jobs.search.url}") String searchUrl,
            @Value("${jobs.search.keywords}") String keywords,
            @Value("${jobs.search.geoId}") String geoID,
            @Value("${jobs.search.places}") String places
    ) {
        this.url = searchUrl;
        this.keywords = keywords;
        this.geoId = geoID;
        this.places = places;
    }

    @Override
    public Document apply(Integer timePeriodHours) {
        try {
            Connection con = Jsoup.connect(url).data("keywords", keywords).data("geoId", geoId).data("f_PP", places).data("f_TPR", "r" + timePeriodHours * 60 * 60);
            Document document = con.userAgent(USER_AGENT).get();
            log.info("Linkedin job search from url: {}", document.location());
            return document;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
