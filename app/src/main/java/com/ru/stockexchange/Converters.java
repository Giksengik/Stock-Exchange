package com.ru.stockexchange;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Converters {
    public static String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
    public static String getCurrencySymbol(String currency) {
        // API странно работает : указывает валюту , которая не равна USD($), но цену указывает в американских долларах, поэтому выбор без выбора
        return "$";
    }

    // Формат даты YYYY-MM-DD....
    public static String parseDate(String date){
        int yearNum = 0;
        int monthNum = 0;
        String month;
        int dayNum = 0;
        int count = 0;
        StringBuilder currentString = new StringBuilder();
        for (int i = 0; i < date.length(); i++ ) {
            if(date.charAt(i) == '-' || date.charAt(i) == 'T'){
                switch(count){
                    case 0:
                        yearNum = Integer.parseInt(currentString.toString());
                        currentString.setLength(0);
                        count ++;
                        break;
                    case 1:
                        monthNum = Integer.parseInt(currentString.toString());
                        currentString.setLength(0);
                        count ++;
                        break;
                    case 2:
                        dayNum = Integer.parseInt(currentString.toString());
                        break;
                }
            }else{
                currentString.append(date.charAt(i));
            }
        }
        switch(monthNum){
            case 2:
                month = "feb";
                break;
            case 3:
                month = "mar";
                break;
            case 4:
                month = "apr";
                break;
            case 5:
                month = "may";
                break;
            case 6:
                month = "jun";
                break;
            case 7:
                month = "jul";
                break;
            case 8:
                month = "aug";
                break;
            case 9:
                month = "sep";
                break;
            case 10:
                month = "oct";
                break;
            case 11:
                month = "nov";
                break;
            case 12:
                month = "dec";
                break;
                default:
                month = "jan";
        }
        return (dayNum + " " +  month + " " + yearNum);
    }
}
