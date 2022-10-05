package com.handson.chatbot.service;

import com.handson.chatbot.controller.BotController;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AmazonService {
    public static final Pattern PRODUCT_PATTERN = Pattern.compile("<span class=\\\"a-size-medium a-color-base a-text-normal\\\">([^<]+)<\\/span>.*<span class=\\\"a-icon-alt\\\">([^<]+)<\\/span>.*<span class=\\\"a-offscreen\\\">([^<]+)<\\/span>");
    public String searchProducts(String keyword) throws IOException {
        return parseProductHtml(getProductHtml(keyword));
    }

    private String parseProductHtml(String html) {
        String res = "";
        Matcher matcher = PRODUCT_PATTERN.matcher(html);
        for( int i = 0 ; i < 10 && matcher.find() ; i++) {
            res += (i + 1) + ") " + matcher.group(1) + " - " + matcher.group(2) + ", price: " + matcher.group(3) + "\n";
        }
        return res;
    }

    private String getProductHtml(String keyword) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://www.amazon.de/s?k=" + keyword + "&crid=2FWBJ43GWKIJY&sprefix=ipod%2Caps%2C68&ref=nb_sb_noss_1")
                .get()
                .addHeader("authority", "www.amazon.de")
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("accept-language", "en-US,en;q=0.9")
                .addHeader("cookie", "session-id=257-7488118-2434214; i18n-prefs=EUR; ubid-acbde=258-7699191-2168361; session-token=PPwcq/lVM6DLusM8cvuIYlKRAVoxDhBKjsPBlwtsp0mKo4w4ycnRDvseFDs5maQHfCePzeXRLwOir8agQh/0dQnYEa79+V9SYbxgQRZ5HH5LDVvrL069yhowzJWOIp3NfGm0G6ejvxnvN0uF/FWIRakblw/fnQZgmL8BDDjBIYMvWD+CmZ2d5P8J5JkVIWk3B0FT5BPy4z46lIyhYtStWDu+fxQwkzE3; lc-acbde=en_GB; session-id-time=2082787201l; csm-hit=tb:6A94ANZJKFY6QSX4ZDCZ+s-6AM14GF59X1N8GABEB9Y|1663494095498&t:1663494095498&adb:adblk_no")
                .addHeader("device-memory", "8")
                .addHeader("downlink", "10")
                .addHeader("dpr", "1")
                .addHeader("ect", "4g")
                .addHeader("referer", "https://www.amazon.de/-/en/ref=nav_logo")
                .addHeader("rtt", "0")
                .addHeader("sec-ch-device-memory", "8")
                .addHeader("sec-ch-dpr", "1")
                .addHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"101\", \"Google Chrome\";v=\"101\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Linux\"")
                .addHeader("sec-ch-viewport-width", "1536")
                .addHeader("sec-fetch-dest", "document")
                .addHeader("sec-fetch-mode", "navigate")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.0.0 Safari/537.36")
                .addHeader("viewport-width", "1536")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
