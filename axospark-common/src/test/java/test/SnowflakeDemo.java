package test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class SnowflakeDemo {

    public static void main(String[] args) {
        long n = -1L;
        System.out.println(Long.toBinaryString(31));
        LocalDateTime time = LocalDateTime.of(2025, 10, 20, 0, 0, 0);
        Instant instant = time.toInstant(ZoneOffset.UTC);
        long second = instant.toEpochMilli();

        System.out.println("second = " + second);
    }
}
