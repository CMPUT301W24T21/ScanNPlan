package com.example.project_3;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AttendeeTest {

    @Rule
    public ActivityScenarioRule<AttendeeActivity> scenario = new ActivityScenarioRule<AttendeeActivity>(AttendeeActivity.class);

    @Test
    public void HomepageToEventDetails1(){
        onView(withId(R.id.event_listView)).check(matches(isDisplayed()));
        onView(withId(R.id.EditProfile)).check(matches(isDisplayed()));
        onView(withId(R.id.openCameraButton)).check(matches(isDisplayed()));
        onView(withText("concert1")).perform(click());
        onView(withId((R.id.appbar_title))).check(matches((isDisplayed())));
        onView(withId((R.id.event_poster))).check(matches((isDisplayed())));
        onView(withId((R.id.event_date))).check(matches((isDisplayed())));
        onView(withId((R.id.event_time))).check(matches((isDisplayed())));
        onView(withId((R.id.event_details))).check(matches((isDisplayed())));
        onView(withId((R.id.back_button))).check(matches((isDisplayed())));
        onView(withId(R.id.back_button)).perform(click());
        onView(withId(R.id.event_listView)).check(matches(isDisplayed()));
        onView(withId(R.id.EditProfile)).check(matches(isDisplayed()));
        onView(withId(R.id.openCameraButton)).check(matches(isDisplayed()));
    }

    @Test
    public void HomepageToEventDetails2(){
        onView(withId(R.id.event_listView)).check(matches(isDisplayed()));
        onView(withId(R.id.EditProfile)).check(matches(isDisplayed()));
        onView(withId(R.id.openCameraButton)).check(matches(isDisplayed()));
        onView(withText("party2")).perform(click());
        onView(withId((R.id.appbar_title))).check(matches((isDisplayed())));
        onView(withId((R.id.event_poster))).check(matches((isDisplayed())));
        onView(withId((R.id.event_date))).check(matches((isDisplayed())));
        onView(withId((R.id.event_time))).check(matches((isDisplayed())));
        onView(withId((R.id.event_details))).check(matches((isDisplayed())));
        onView(withId((R.id.back_button))).check(matches((isDisplayed())));
        onView(withId(R.id.back_button)).perform(click());
        onView(withId(R.id.event_listView)).check(matches(isDisplayed()));
        onView(withId(R.id.EditProfile)).check(matches(isDisplayed()));
        onView(withId(R.id.openCameraButton)).check(matches(isDisplayed()));
    }

    @Test
    public void HomepageToProfileSettings() throws InterruptedException {
        //onView(withId(R.id.event_listView)).check(matches(isDisplayed()));
        onView(withId(R.id.EditProfile)).check(matches(isDisplayed()));
        onView(withId(R.id.openCameraButton)).check(matches(isDisplayed()));
        onView(withId(R.id.EditProfile)).perform(click());

        onView(withId((R.id.upload_new_button))).check(matches((isDisplayed())));
        onView(withId((R.id.delete_photo_button))).check(matches((isDisplayed())));
        onView(withId((R.id.homepage_editText))).check(matches((isDisplayed())));
        onView(withId((R.id.contact_info_editText))).check(matches((isDisplayed())));
        onView(withId((R.id.push_notifications_toggle))).check(matches((isDisplayed())));
        onView(withId((R.id.geolocation_toggle))).check(matches((isDisplayed())));
        onView(withId((R.id.save_button))).check(matches((isDisplayed())));
        onView(withId((R.id.logout_button))).check(matches((isDisplayed())));
        onView(withId((R.id.back_button))).check(matches((isDisplayed())));

        onView(withId(R.id.back_button)).perform(click());
        //onView(withId(R.id.event_listView)).check(matches(isDisplayed()));
        onView(withId(R.id.EditProfile)).check(matches(isDisplayed()));
        onView(withId(R.id.openCameraButton)).check(matches(isDisplayed()));
    }

}
