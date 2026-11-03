package com.kakeibo.kakeibo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * 年月文字列を "YYYY-MM" 形式に統一する。
 * "2026-1" のようなゼロ埋めなしの値も受け付け、"2026-01" に正規化する。
 */
public final class MonthUtil {

    private static final Pattern MONTH = Pattern.compile("^(\\d{4})-(\\d{1,2})$");
    private static final Pattern DATE = Pattern.compile("^(\\d{4})-(\\d{2})-(\\d{2})$");

    private MonthUtil() {
    }

    public static String normalizeMonth(String value) {
        if (value == null) {
            throw badRequest("年月が指定されていません");
        }
        Matcher m = MONTH.matcher(value.trim());
        if (!m.matches()) {
            throw badRequest("年月は YYYY-MM 形式で指定してください: " + value);
        }
        int month = Integer.parseInt(m.group(2));
        if (month < 1 || month > 12) {
            throw badRequest("月の値が不正です: " + value);
        }
        return String.format("%s-%02d", m.group(1), month);
    }

    /** "YYYY-MM-DD" の日付文字列を検証して返す */
    public static String validateDate(String value) {
        if (value == null || !DATE.matcher(value.trim()).matches()) {
            throw badRequest("日付は YYYY-MM-DD 形式で指定してください: " + value);
        }
        return value.trim();
    }

    /** "YYYY-MM-DD" から "YYYY-MM" を取り出す */
    public static String monthOf(String date) {
        return validateDate(date).substring(0, 7);
    }

    private static ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
