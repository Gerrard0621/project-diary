package com.ezen.diary;

public class DiaryApp {
    public static void main(String[] args) {
        com.ezen.diary.Diary diary = new com.ezen.diary.Diary();
        diary.init();
        diary.setSize(700, 500);
        diary.addEventListener();
        diary.setVisible(true);
    }
}
