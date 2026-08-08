package com.ayoub.url_shortener.util;

public class Base62 {

    private static final String AVAILABLE_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = AVAILABLE_CHARS.length();

    public static String encode(Long number){
        if(number < 0){
            throw new IllegalArgumentException("number shouldn't be negative");
        }

        if(number == 0){
            return "0";
        }

        StringBuilder sb = new StringBuilder();

        while(number > 0){
            int nbr = (int) (number % BASE);
            sb.append(AVAILABLE_CHARS.charAt(nbr));
            number /= BASE;
        }

        return sb.reverse().toString();

    }


    public static long decode(String base62Str){
        if(base62Str == null || base62Str.isEmpty()){
            throw new IllegalArgumentException("base62Str shouldn't be null or empty");
        }

        long result = 0;

        for(int i = 0; i< base62Str.length(); i++){
            char c = base62Str.charAt(i);
            int index = AVAILABLE_CHARS.indexOf(c);

            if(index == -1){
                throw new IllegalArgumentException(" the string given contains illegal character outside of Base62(0-9a-zA-Z)");
            }

            result += result * BASE + index;

        }

        return result;
    }



}
