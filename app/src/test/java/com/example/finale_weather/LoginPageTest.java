package com.example.finale_weather;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class LoginPageTest {

    @Test
    public void correctEmailValidator()
    {
        Assert.assertTrue(LoginPage.EmailValidation("abc@email.com"));
        Assert.assertTrue(LoginPage.EmailValidation("ab123.c@email.com"));
        Assert.assertTrue(LoginPage.EmailValidation("abc_xyz@email.com"));
    }
    @Test
    public void incorrectEmailValidator()
    {
        Assert.assertFalse(LoginPage.EmailValidation("@email.com"));
        Assert.assertFalse(LoginPage.EmailValidation("abc"));
        Assert.assertFalse(LoginPage.EmailValidation("abc@email."));
        Assert.assertFalse(LoginPage.EmailValidation(".com"));


    }

}