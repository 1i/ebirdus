package com.onei.ebirdus;

import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Slf4j
public class Utils {

    public static LocalDate getDateFromDay(String input) {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(input.toUpperCase());
        int differenceOfDays = today.minus(dayOfWeek.getValue()).getValue();
        return LocalDate.now().minusDays(differenceOfDays);
    }

    public static LocalDate formatDate(String date) {
        LocalDate formattedDate = null;
        try {
            formattedDate = LocalDate.parse(date);
        } catch (DateTimeParseException dte) {
            log.error("DateTimeParseException ", dte);
            formattedDate = LocalDate.now();
            log.info("DTPE fetch for [{}]", formattedDate);
        }
        return formattedDate;
    }
}
