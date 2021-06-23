package com.rino.homework06.utils;

import android.content.Context;
import android.widget.Toast;

import com.rino.homework06.MainActivity;
import com.rino.homework06.R;
import com.rino.homework06.entities.Note;
import com.rino.homework06.entities.Priority;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {

    public static List<Note> getListOfNotes() {
        List<Note> notes = new ArrayList<>();

        notes.add(
                new Note(
                        "Купить продукты",
                        "Необходимо купить: овощи, фрукты, хлеб и т.д.",
                        new Date(1623312000000L),
                        Priority.NORMAL
                )
        );

        notes.add(
                new Note(
                        "Изучить паттерны проектирования",
                        "Для этого необходимо купить и прочитать книгу с сайта https://refactoring.guru/ru",
                        new Date(1623398400000L),
                        Priority.HIGH
                )
        );

        notes.add(
                new Note(
                        "Сделать домашнее задание №6",
                        "Нужно прочитать методичку для закрепления видео материала.",
                        new Date(1623486600000L),
                        Priority.NORMAL
                )
        );

        notes.add(
                new Note(
                        "Расписание занятий GeekBrains",
                        "Занятия проходят в среду и воскресенье.",
                        new Date(1623761025000L),
                        Priority.NORMAL
                )
        );

        return notes;
    }

    public static void showToastShort(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String message, int toastLength) {
        Toast.makeText(context, message, toastLength).show();
    }
}
