package com.example.project_3;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//import androidx.test.core.app.ApplicationProvider;
//import androidx.test.espresso.action.ViewActions;
//import androidx.test.espresso.intent.rule.IntentsTestRule;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.filters.LargeTest;
//
//import com.google.firebase.FirebaseApp;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(AndroidJUnit4.class)
//@LargeTest
//public class OrganizerActivityTest {
//    @Rule
//    public ActivityScenarioRule<OrganizerActivity> scenario = new
//            ActivityScenarioRule<>(OrganizerActivity.class);
//    @Rule
//    public IntentsTestRule<OrganizerActivity> intentsRule =
//            new IntentsTestRule<>(OrganizerActivity.class);
//    @Before
//    public void setup() {
//        // Initialize Firebase
//        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
//    }
//    @Test
//    public void testAddCity() {
//        // Click on Add Event button
//        onView(withId(R.id.fab_add_event)).perform(click());
//        onView(withId(R.id.add_event_editText)).perform(ViewActions.typeText("Test Event"));
//        onView(withId(R.id.ok_button)).perform(click());
//        onView(withText("Test Event")).check(matches(isDisplayed()));
//    }
//}

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

import com.google.firebase.firestore.FirebaseFirestore;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.*;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static org.hamcrest.CoreMatchers.not;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import org.hamcrest.CoreMatchers;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class OrganizerActivityTest {

    @Rule
    public IntentsTestRule<OrganizerActivity> intentsTestRule = new IntentsTestRule<>(OrganizerActivity.class);

    private FirebaseFirestore db;

    @Before
    public void setUp() {
        db = FirebaseFirestore.getInstance();
    }

    @Test
    public void testAddEvent() {
        // Click on the add event FAB
        onView(withId(R.id.fab_add_event)).perform(click());
        // Enter event details in the dialog and click OK
        onView(withId(R.id.add_event_editText)).perform(ViewActions.typeText("Test"), closeSoftKeyboard());
        onView(withId(R.id.ok_button)).perform(click());
        onView(withId(R.id.event_list)).perform(swipeUp());
        // Check if the last item is displayed
        onView(withText("Test")).check(matches(isDisplayed()));
    }
}
