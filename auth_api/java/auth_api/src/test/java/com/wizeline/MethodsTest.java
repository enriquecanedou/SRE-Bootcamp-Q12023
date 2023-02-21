package com.wizeline;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class MethodsTest {

    @Test
    void generateToken() throws SQLException {
        Assertions.assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYWRtaW4ifQ.ZXUtF8Um85g6rKtsBVny_gGBoAljA4WDfe2YTuj5Tqs", Methods.generateToken("admin", "secret"));
    }

    @Test
    void accessData() {
        Assertions.assertEquals("You are under protected data", Methods.accessData("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYWRtaW4ifQ.StuYX978pQGnCeeaj2E1yBYwQvZIodyDTCJWXdsxBGI"));
    }
}