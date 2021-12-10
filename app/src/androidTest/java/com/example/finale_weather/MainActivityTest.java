package com.example.finale_weather;



import android.app.Activity;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withInputType;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAssertion;
import androidx.test.ext.junit.rules.ActivityScenarioRule;


import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.regex.Matcher;

@RunWith(JUnit4.class)
public class MainActivityTest extends TestCase {

@Rule
    public ActivityScenarioRule myActivity=new ActivityScenarioRule(MainActivity.class);


    @Test
    public void ValidateEditInputTest()
    {
        Espresso.onView(withId(R.id.idtextFieldInput)).perform(typeText("Delhi")).check(matches(withText("Delhi")));

    }
    @Test
    public void SearchButton()
    {
        Espresso.onView(withId(R.id.idSearchImage)).perform(click());
    }


}