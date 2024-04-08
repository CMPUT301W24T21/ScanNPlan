package com.example.project_3;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.ComponentName;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import org.junit.Test;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
@RunWith(AndroidJUnit4.class)
@LargeTest
public class
EventDetailsTest {
    @Test
    public void testBackButton() {
        // Create a test event name
        String eventName = "Zero";
        String eventLocation = "Test Location";
        String eventDate = "Test Date";
        String eventTime = "Test Time";
        String eventDetails = "Test Details";

        // Use the activity to create an intent
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), EventDetailsActivity.class);

        // Put the event name as an extra to the intent
        intent.putExtra("eventName", eventName);
        intent.putExtra("eventLocation", eventLocation);
        intent.putExtra("eventDate", eventDate);
        intent.putExtra("eventTime", eventTime);
        intent.putExtra("eventDetails", eventDetails);

        // Initialize Intents
        Intents.init();

        // Launch the activity with the intent
        ActivityScenario<EventDetailsActivity> scenario = ActivityScenario.launch(intent);

        // Now you can perform your testing actions on the activity
        // For example, you can check if the event name is correctly displayed
        onView(withId(R.id.event_name_text_view)).check(matches(withText(eventName)));
        onView(withId(R.id.date_event)).check(matches(withText(eventDate)));
        onView(withId(R.id.location_event)).check(matches(withText(eventLocation)));
        onView(withId(R.id.details_event)).check(matches(withText(eventDetails)));

        // Simulate pressing the back button
        onView(withId(R.id.back_button)).perform(click());

        // Check if the intent to start the OrganizerActivity is sent
        intended(hasComponent(OrganizerActivity.class.getName()));

        // Release Intents
        Intents.release();
    }
}
