package com.example.fireside.controller;

import com.example.fireside.entity.Holiday;
import com.example.fireside.entity.Month;
import com.example.fireside.entity.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HolidaysController {

    private final List<Holiday> result = new ArrayList<>();
    private String DATE = String.valueOf(LocalDateTime.now().getDayOfMonth());
    private String MONTH = String.valueOf(LocalDateTime.now().getMonthValue());
    private String STATE = "";
    private String URL = "https://www.calend.ru/holidays/" + MONTH + "-" + DATE;

    public void parseHolidays() throws IOException {
        Document document = Jsoup.connect(URL)
                .userAgent("Google Chrome")
                .timeout(5000)
                .referrer("https://google.com")
                .get();
        Elements holidays = document.select("body > div.wrapper > div.block.main" +
                " > div.block.content > div.block.datesList > div.block.holidays > ul.itemsNet > li.three-three" +
                " > div.caption");
        Elements images = document.select("body > div.wrapper > div.block.main > div.block.content" +
                " > div.block.datesList > div.block.holidays > ul.itemsNet > li.three-three" +
                " > div.image > a > img");
        if (holidays.isEmpty()) {
            throw new IOException("Ошибка! Проверьте правильность введенных вами данных.");
        }
        if (!STATE.equals("")) {
            for (int i = 0; i < holidays.size(); i++) {
                if (!holidays.get(i).select(" > div.link > a").text().equals(STATE)) {
                    images.remove(i);
                    holidays.remove(i);
                    i--;
                }
            }
            if (holidays.isEmpty()) {
                throw new IOException(STATE + " в этот день не отмечаются!");
            }
        }
        for (int i = 0; i < holidays.size(); i++) {
            Document eachPage = Jsoup.connect(holidays.select(" > span.title > a")
                            .eachAttr("href").get(i))
                    .userAgent("Google Chrome")
                    .timeout(5000)
                    .referrer("https://google.com")
                    .get();
            Elements about = eachPage.select("body > div.wrapper > div.block.main" +
                    " > div.block.content > div.maintext > p");
            StringBuilder description = new StringBuilder();
            if (about.hasText()) {
                for (Element e : about) {
                    description.append(e.text());
                }
            } else {
                description = new StringBuilder("Невозможно найти описание для данного праздника!");
            }
            result.add(new Holiday(holidays.select(" > span.title > a").eachText().get(i),
                    description.toString(),
                    images.eachAttr("src").get(i)));
        }
    }

    @GetMapping("/holidays")
    public String getHolidays(@AuthenticationPrincipal User user, Model model) {
        if (!result.isEmpty()) {
            result.clear();
        }
        try {
            parseHolidays();
        } catch (IOException e) {
            model.addAttribute("exception", e.getMessage());
        }
        DATE = String.valueOf(LocalDateTime.now().getDayOfMonth());
        MONTH = String.valueOf(LocalDateTime.now().getMonthValue());
        STATE = "";
        URL = "https://www.calend.ru/holidays/" + MONTH + "-" + DATE;
        model.addAttribute("holidays", result);
        model.addAttribute("user", user);
        return "holidays";
    }

    @PostMapping("/holidays")
    public String getDayAndMonth(@RequestParam String day, @RequestParam Month month, @RequestParam String state) {
        DATE = day;
        MONTH = month.getTitle();
        STATE = state;
        this.URL = "https://www.calend.ru/holidays/" + MONTH + "-" + DATE;
        return "redirect:/holidays";
    }
}
