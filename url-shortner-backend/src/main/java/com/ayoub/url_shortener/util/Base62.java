package com.ayoub.url_shortener.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

public class Base62 {

    private static final String AVAILABLE_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = AVAILABLE_CHARS.length();
    private static final BigInteger BASE_UUID = BigInteger.valueOf(62);

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


    public static String encodeUUID(UUID uuid){
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());

        BigInteger bigInteger = new BigInteger(1, byteBuffer.array());

        if(bigInteger.equals(BigInteger.ZERO)){
            return "0";
        }

        StringBuilder sb = new StringBuilder();

        while(bigInteger.compareTo(BigInteger.ZERO) > 0){
            BigInteger[] divideAndRemainder = bigInteger.divideAndRemainder(BASE_UUID);
            int nbr = divideAndRemainder[1].intValue();
            sb.append(AVAILABLE_CHARS.charAt(nbr));
            bigInteger = divideAndRemainder[0];
        }
        return sb.reverse().toString();
    }


    public static UUID decodeUUID(String base62Str){
        if(base62Str == null || base62Str.isEmpty()){
            throw new IllegalArgumentException("base62Str shouldn't be null or empty");
        }

        BigInteger result = BigInteger.ZERO;

        for(int i = 0; i< base62Str.length(); i++){
            char c = base62Str.charAt(i);
            int index = AVAILABLE_CHARS.indexOf(c);

            if(index == -1){
                throw new IllegalArgumentException(" the string given contains illegal character" + c + " outside of Base62(0-9a-zA-Z)");
            }

            result =  result.multiply(BASE_UUID).add(BigInteger.valueOf(index));
        }

        byte[] bytes = result.toByteArray();
        byte[] uuidBytes = new byte[16];

        if(bytes.length > 16){
            System.arraycopy(bytes, bytes.length - 16, uuidBytes, 0,16);
        }else {
            System.arraycopy(bytes, 0, uuidBytes, 16 - bytes.length, bytes.length);
        }

        ByteBuffer byteBuffer = ByteBuffer.wrap(uuidBytes);
        long mostSignificantBits = byteBuffer.getLong();
        long leastSignificantBits = byteBuffer.getLong();

        return new UUID(mostSignificantBits, leastSignificantBits);
    }


}
