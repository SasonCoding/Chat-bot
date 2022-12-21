package com.handson.chatbot.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AmazonService {
    public static final Pattern PRODUCT_PATTERN = Pattern.compile("<span class=\\\"a-size-medium a-color-base a-text-normal\\\">([^<]+)<\\/span>.*<span class=\\\"a-icon-alt\\\">([^<]+)<\\/span>.*<span class=\\\"a-offscreen\\\">([^<]+)<\\/span>");


    public String searchProducts(String keyword) throws IOException {
        return parseProductHtml(getProductHtml(keyword));
    }

    private String parseProductHtml(String html) {
        Integer i = 1;
        String res = "";
        Matcher matcher = PRODUCT_PATTERN.matcher(html);
        while (matcher.find()) {
            res += i + ") " + matcher.group(1) + " - " + matcher.group(2) + ", price:" + matcher.group(3) + "<br>\n";
            i++;
        }
        return res;
    }

    private String getProductHtml(String keyword) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://www.amazon.com/s?i=aps&k=" + keyword + "&ref=nb_sb_noss&url=search-alias%3Daps")
                .method("GET", null)
                .addHeader("authority", "www.amazon.com")
                .addHeader("rtt", "150")
                .addHeader("downlink", "10")
                .addHeader("ect", "4g")
                .addHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"98\", \"Google Chrome\";v=\"98\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"macOS\"")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.80 Safari/537.36")
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("sec-fetch-mode", "navigate")
                .addHeader("sec-fetch-user", "?1")
                .addHeader("sec-fetch-dest", "document")
                .addHeader("referer", "https://www.amazon.com/s?k=ipod&crid=10ML3RYZ6VI78&sprefix=ipod%2Caps%2C235&ref=nb_sb_noss_2")
                .addHeader("accept-language", "en-US,en;q=0.9,he;q=0.8")
                .addHeader("cookie", "session-id=131-3987483-5342545; session-id-time=2082787201l; i18n-prefs=USD; sp-cdn=\"L5Z9:IL\"; ubid-main=133-9581942-3780819; session-token=Lt+SK9WSLG/W355W1XWSVapZq/ZGkHRManYdqDH89VqLlD4usX2CuEND0wAyE/Rssnj7J0Md84SixshLUARc0GYoRxc5JJ/YlDNJDtGGiE3TFLer74TU8fLsk4ACTltqJ1zhd+nHQMgbALM1iOJKUXbdcG2/6qzTJmuQvl9yOhwzw0mV7OWQrGzfyc7QZTpW; skin=noskin; csm-hit=tb:S8PVFX7Y80DF58KAWHE1+s-JSBQAF9YBWCKDMKNE2GC|1644690626446&t:1644690626446&adb:adblk_yes; session-token=8hFpbxGA0LLmAUbJEVhyRQSRo2SACBw9zr3/D04iSCtfarTBoZQnIq6oH05SSUbx7Q6/IlCDnCceUoM6Y17FI5tbXCstRmMRart/9TXcZAFJ+YWhRmAX0iMSK3Y8POzcArmKmy5iF1CLANDb4LeNpfyRTH3qTk/e7OkmrNZrWMjeR9o8K27B+gHwGI301yBl")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
