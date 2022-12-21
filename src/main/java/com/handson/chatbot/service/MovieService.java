package com.handson.chatbot.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MovieService {

    public static final Pattern MOVIES_PATTERN = Pattern.compile("class=\\\"ipc-metadata-list-summary-item__t\\\" role=\\\"button\\\" tabindex=\\\"0\\\" aria-disabled=\\\"false\\\" href=\\\"/title/[a-z0-9]+/\\?ref_=fn_tt_tt_\\d\\d?\\\">([^<]+)</a><ul class=\\\"ipc-inline-list ipc-inline-list--show-dividers ipc-inline-list--no-wrap ipc-inline-list--inline ipc-metadata-list-summary-item__tl base\\\" role=\\\"presentation\\\"><li role=\\\"presentation\\\" class=\\\"ipc-inline-list__item\\\"><label class=\\\"ipc-metadata-list-summary-item__li\\\" role=\\\"button\\\" tabindex=\\\"0\\\" aria-disabled=\\\"false\\\" for=\\\"_blank\\\">([0-9]+)<");

    public String searchMovies(String keyword) throws IOException {
        return parseMoviesHtml(getMoviesHtml(keyword));
    }

    private String parseMoviesHtml(String html) {
        String res = "";
        Matcher matcher = MOVIES_PATTERN.matcher(html);
        while (matcher.find()) {
            res += matcher.group(1) + " - " + matcher.group(2) + "\n";
        }
        return res;
    }

    private String getMoviesHtml(String keyword) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://www.imdb.com/find?q=" + keyword + "&s=tt&ref_=fn_al_tt_mr")
                .get()
                .addHeader("authority", "www.imdb.com")
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("accept-language", "he-IL,he;q=0.9,en-US;q=0.8,en;q=0.7")
                .addHeader("cookie", "session-id=134-8169584-5235520; session-id-time=2082787201l; uu=eyJpZCI6InV1OTQ0ZWIxMWY3N2RjNDUzZjhlMWEiLCJwcmVmZXJlbmNlcyI6eyJmaW5kX2luY2x1ZGVfYWR1bHQiOmZhbHNlfX0=; ubid-main=135-8882961-4758923; session-token=BumFfBhxOXx0BYsIZ0iVlZgbMm8lX0kE1mxE6O/lSBsE3s62u3ydFd0kGlN8BqgLVDvI7Evk+75swVoY4xYQEYd8BbaTWCj3ao6ecEBI+LmQbNNZxM27FqzaSxaf1xbUeCrOQYhvrF7ogVbbh0rw0xMFDLPZUWd2t8dx2/hon70p6KktyMY2cQv4IFnV20IHCHccUJlmadFqGtynCC/EedIehFgTMrWG; adblk=adblk_no; csm-hit=tb:Y3NQBBPF763GRZH3WG19+b-XVV9VD5S701QJNGAYND9|1663531863402&t:1663531863402&adb:adblk_no; session-token=yipWDl0DplrCnRNGjcPLSa9GbJ9vnHiuLRmoc8q+jTE5YeOT4VtIcw3IP7N1B3XRZM5vpoPZLpogJ4y5kqKmvCVz56GJmMQS85VtxhuY2SWfloaCekJk8Ed6281AFDNJ1GXOU6JtQ25CnGmV+vpA+RoqqM/MDxalJrhgZ0Mzl9g2PgxEhlyQoPv72+VTxvzK0jUGROT+8PJhId3g14iGXZRn1EzLIdI4")
                .addHeader("referer", "https://www.imdb.com/find?q=black&ref_=nv_sr_sm")
                .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"105\", \"Not)A;Brand\";v=\"8\", \"Chromium\";v=\"105\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("sec-fetch-dest", "document")
                .addHeader("sec-fetch-mode", "navigate")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("sec-fetch-user", "?1")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();

//        OkHttpClient client = new OkHttpClient().newBuilder().build();
//        Request request = new Request.Builder()
//                .url("https://www.imdb.com/find/?q=" + keyword + "&s=tt&ref_=fn_al_tt_mr")
//                .get()
//                .addHeader("authority", "www.imdb.com")
//                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
//                .addHeader("accept-language", "he-IL,he;q=0.9,en-US;q=0.8,en;q=0.7")
//                .addHeader("cache-control", "no-cache")
//                .addHeader("cookie", "session-id=132-0828149-8287649; session-id-time=2082787201l; uu=eyJpZCI6InV1ZDFhMWE2MDY2MDhjNDYzMDkzYjEiLCJwcmVmZXJlbmNlcyI6eyJmaW5kX2luY2x1ZGVfYWR1bHQiOmZhbHNlfX0=; ubid-main=131-5010304-9198560; adblk=adblk_no; session-token=enJTtDiwhdSQTl5obphCIKKCoVPsiod2jMbpiGrUchmXatdgU/N4g7gIMuIurz6RWZjXCwVTNJojnqkTpaiLDzGRZX5AZNmZMns7QHN6Vo1PcZpe+DS19AyJ60MPE9a2plsvczuvfWkYLE1MPctFUymzpdnHKl+dKgTuIGTHxuCe2nKKto3ID5KxbCvO+yy0Zh1SO+AFSGkGe6SzUlxiiA==; csm-hit=tb:s-25VAC097GA03Y29KE78Q|1671614116366&t:1671614116832&adb:adblk_no; session-id=132-0828149-8287649; session-id-time=2082787201l; session-token=hlFKfwIhpG02ALwwV1ABRf4i6teswgvDHWEI/toClXKH8Y4ppFev+MEfSstKOXH4SsCbE0VL2SvizJd6IXW3uo0hA8ksA97LevuPJrJymfG3J9Hmsm3zHBghoN4/Fht7T0jOUKOdpbIGTNZ3ibDUofmDNJ2BAxO1n4YN909JviC5Wa9hg3V9AV5hDnyR1EnSmXXxeEKuGpTeso9gJlKwbw==")
//                .addHeader("pragma", "no-cache")
//                .addHeader("sec-ch-ua", "\"Not?A_Brand\";v=\"8\", \"Chromium\";v=\"108\", \"Google Chrome\";v=\"108\"")
//                .addHeader("sec-ch-ua-mobile", "?0")
//                .addHeader("sec-ch-ua-platform", "\"Windows\"")
//                .addHeader("sec-fetch-dest", "document")
//                .addHeader("sec-fetch-mode", "navigate")
//                .addHeader("sec-fetch-site", "none")
//                .addHeader("sec-fetch-user", "?1")
//                .addHeader("upgrade-insecure-requests", "1")
//                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
//                .build();
//        Response response = client.newCall(request).execute();
//        return response.body().string();
    }
}
