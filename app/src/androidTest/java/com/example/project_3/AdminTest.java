package com.example.project_3;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AdminTest {

    @Rule
    public ActivityScenarioRule<AdminActivity> scenario = new ActivityScenarioRule<AdminActivity>(AdminActivity.class);

    @Test
    public void testAdminSwapProfiles(){
        onView(withId(R.id.manage_profiles)).check(matches(isDisplayed()));
        onView(withId(R.id.manage_events)).check(matches(isDisplayed()));
        onView(withId(R.id.manage_profiles)).perform(click());
        onView(withId(R.id.list_profiles_text)).check(matches(isDisplayed()));
        onView(withId(R.id.back_button)).perform(click());
        onView(withId(R.id.list_profiles_text)).check(doesNotExist());
    }
    @Test
    public void testAdminSwapEvents(){
        onView(withId(R.id.manage_events)).check(matches(isDisplayed()));
        onView(withId(R.id.manage_profiles)).check(matches(isDisplayed()));
        onView(withId(R.id.manage_events)).perform(click());
        onView(withId(R.id.list_events_text)).check(matches(isDisplayed()));
        onView(withId(R.id.back_button)).perform(click());
        onView(withId(R.id.list_events_text)).check(doesNotExist());
    }
    @Test
    public void testAdminSwapImages(){
        onView(withId(R.id.manage_events)).check(matches(isDisplayed()));
        onView(withId(R.id.manage_profiles)).check(matches(isDisplayed()));
        onView(withId(R.id.back_button)).perform(click());
    }
    




}
