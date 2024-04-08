package com.example.project_3;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.ComponentName;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class OrganizerActivityTest {
    @Rule
    public IntentsTestRule<OrganizerActivity> mActivityRule = new IntentsTestRule<>(OrganizerActivity.class);

    // this test is good
    @Test
    public void testActivityChangeOnListPress() {
        // Wait for 10 seconds to load up the cards
        try {
            Thread.sleep(10000); // 20 seconds in milliseconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Click on the ListView item
        onView(withId(R.id.event_list)).perform(click());
        // Check if the intent to start the EventDetailsActivity is sent
        intended(hasComponent(new ComponentName(getTargetContext(), EventDetailsActivity.class)));
    }


    // this test is good
    @Test
    public void testActivityChangeOnNotificationPress() {
        // Click on the notifications button
        onView(withId(R.id.notif_button)).perform(click());
        // Check if the intent to start the NotificationsPage is sent
        intended(hasComponent(new ComponentName(getTargetContext(), NotificationsPage.class)));
    }


    @Test
    public void testClickOnEvent() {
        // Wait for 10 seconds to load up the cards
        try {
            Thread.sleep(10000); // 10 seconds in milliseconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click on the first event in the list
        onView(withId(R.id.event_list)).perform(click());

        // Check if the intent to start the EventDetailsActivity is sent
        intended(hasComponent(new ComponentName(getTargetContext(), EventDetailsActivity.class)));

    }

    @Test
    public void testClickOnAttendeeInCheckedInList() {
        // Click on the ListView item to open EventDetailsActivity
        onView(withId(R.id.event_list)).perform(click());

        // Click on the check-in button
        onView(withId(R.id.check_ins)).perform(click());

        // testing clicking on an attendee in the checked-in list
        onView(withId(R.id.attendees_list_view)).perform(click()); // Adjust the ID as per your layout
    }

    @Test
    public void testSetMaxAttendees() {
        // Click on the ListView item to open EventDetailsActivity
        onView(withId(R.id.event_list)).perform(click());

        // Click on the check-in button
        onView(withId(R.id.sign_ups)).perform(click());

        // testing clicking on an attendee in the checked-in list
        onView(withId(R.id.max_attendees_edittext)).perform(click()); // Adjust the ID as per your layout
    }

    @Test
    public void testSignUpButtonAfterEventClicked() {
        // Click on the ListView item to open EventDetailsActivity
        onView(withId(R.id.event_list)).perform(click());

        // testing the sign-up button
        testSignUpButton();
    }

    @Test
    public void testCheckInButtonAfterEventClicked() {

        try {
            Thread.sleep(5000); // 5 seconds in milliseconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Click on the ListView item to open EventDetailsActivity
        onView(withId(R.id.event_list)).perform(click());

        testCheckInButton();

    }


    @Test
    public void testEditEventButtonAfterEventClicked() {
        // Click on the ListView item to open EventDetailsActivity
        onView(withId(R.id.event_list)).perform(click());

        // Click on the edit event button
        onView(withId(R.id.floatingEditButton)).perform(click());

        // Check if the intent to start the EditEventActivity is sent
        intended(hasComponent(new ComponentName(getTargetContext(), EditEventDetails.class)));
    }


    private void testSignUpButton() {
        // Click on the sign-up button
        onView(withId(R.id.sign_ups)).perform(click());
        // Check if the intent to start the SignUpActivity is sent
        intended(hasComponent(new ComponentName(getTargetContext(), AttendeesSignedUpActivity.class)));
    }

    private void testCheckInButton() {
        // Click on the check-in button
        onView(withId(R.id.check_ins)).perform(click());
        // Check if the intent to start the CheckInActivity is sent
        intended(hasComponent(new ComponentName(getTargetContext(), AttendeesCheckedInActivity.class)));
    }

    private void testMapButton() {
        // Click on the map button
        onView(withId(R.id.map_button)).perform(click());
        // Check if the intent to start the MapActivity is sent
        intended(hasComponent(new ComponentName(getTargetContext(), EventMapFragment.class)));
    }

    @Test
    public void testEditEventButton() {
        // Click on the ListView item to open EventDetailsActivity
        onView(withId(R.id.event_list)).perform(click());

        // Click on the edit event button
        onView(withId(R.id.floatingEditButton)).perform(click());
    }

    @Test
    public void testViewQRAfterEditEventButton() {
        // Click on the ListView item to open EventDetailsActivity
        onView(withId(R.id.event_list)).perform(click());

        // Click on the edit event button
        onView(withId(R.id.floatingEditButton)).perform(click());

        // Check if the intent to start the EditEventActivity is sent
        intended(hasComponent(new ComponentName(getTargetContext(), EditEventDetails.class)));

        testViewQRButton();
    }

    private void testViewQRButton() {
        // Click on the check-in button
        onView(withId(R.id.view_qrcode)).perform(click());
        // Check if the intent to start the CheckInActivity is sent
        intended(hasComponent(new ComponentName(getTargetContext(), QRCodeDisplayActivity.class)));
    }


    // this test is good

    @Test
    public void testAddEvent() {
        try {
            Thread.sleep(10000); // 10 seconds in milliseconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Click on the add button to open the dialog
        onView(withId(R.id.fab_add_event)).perform(click());

        // Type event name in the EditText
        onView(withId(R.id.add_event_editText)).perform(typeText("Zero"), closeSoftKeyboard());
        onView(withId(R.id.add_date_editText)).perform(typeText("Test Date"), closeSoftKeyboard());
        onView(withId(R.id.add_time_editText)).perform(typeText("Test Time"), closeSoftKeyboard());
        onView(withId(R.id.add_location_editText)).perform(typeText("Test Location"), closeSoftKeyboard());
        onView(withId(R.id.add_details_editText)).perform(typeText("Test Details"), closeSoftKeyboard());

        // Click on the OK button to confirm event addition
        onView(withText("OK")).perform(click());

        // Verify if the event appears in the window
        onView(withText("Zero")).check(matches(isDisplayed()));
    }
}