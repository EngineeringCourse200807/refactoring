package com.odde.securetoken;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class BirthdaytTest {
    @Test
    public void ture_test() {
        Birthday birthday = new Birthday();
        boolean flag = birthday.isBirthday();
        System.out.println("结果" + flag);
        assertTrue(flag);
    }


}
