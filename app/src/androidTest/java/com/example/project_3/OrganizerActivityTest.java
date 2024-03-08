package com.example.project_3;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import android.app.Instrumentation;
import android.content.DialogInterface;
import android.widget.Button;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.filters.MediumTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class OrganizerActivityTest {
    @Rule
    public ActivityScenarioRule<OrganizerActivity> scenario = new
            ActivityScenarioRule<>(OrganizerActivity.class);
//    @Test
//    public void testAddCity(){
//        // Click on Add Event button
//        onView(withId(R.id.fab_add_event)).perform(click());
//        onView(withId(R.id.add_event_editText)).perform(ViewActions.typeText("TLOU watch party"));
//        onView(withId(R.id.add_event_editText)).perform(pressImeActionButton());
//        onView(withText("OK")).perform(click());
//        onView(withText("TLOU watch party")).check(matches(isDisplayed()));
//    }

    @Test
    public void testActivitySwitch() {
        // Test whether the activity correctly switches
        onView(withId(R.id.fab_add_event)).perform(click());
        onView(withId(R.id.add_event_editText)).perform(ViewActions.typeText("TLOU watch party"));
        onView(withId(R.id.add_event_editText)).perform(pressImeActionButton());
        onView(withText("OK")).perform(click());
        // Click on ListView Text
//        onData(anything()).inAdapterView(withId(R.id.event_list)).atPosition(0).perform(click());
        // Check if the screen has switched to the ShowActivity
//        onView(withId(R.id.event_activity_layout)).check(matches(isDisplayed()));
    }

}