package com.odde.securetoken;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.mockito.junit.MockitoJUnit;

public class AuthenticationServiceTest {

    @Test
    public void is_valid_test() {
        AuthenticationService target = new AuthenticationService();

        boolean actual = target.isValid("joey", "91000000");

        assertTrue(actual);
    }


    class Ramdom extends RsaTokenDao {
        public String getRandom(String account) {
//        Random seed = new Random((int) System.currentTimeMillis() & 0x0000FFFF);
//        String result = String.format("%06d", seed.nextInt(999999));
//        System.out.println(String.format("randomCode:%s", result));

            return "000000";
        }
    }
}