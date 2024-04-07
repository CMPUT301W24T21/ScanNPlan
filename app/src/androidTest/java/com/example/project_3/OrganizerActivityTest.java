package com.example.project_3;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class OrganizerActivityTest {
    @Rule
    public IntentsTestRule<OrganizerActivity> mActivityRule = new IntentsTestRule<>(OrganizerActivity.class);
    // this test is good
//    @Test
//    public void testActivityChangeOnListPress() {
//        // Wait for 10 seconds to load up the cards
//        try {
//            Thread.sleep(10000); // 20 seconds in milliseconds
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        // Click on the ListView item
//        onView(withId(R.id.event_list)).perform(click());
//        // Check if the intent to start the EventDetailsActivity is sent
//        intended(hasComponent(new ComponentName(getTargetContext(), EventDetailsActivity.class)));
//    }

//    @Test
//    public void testBackButtonAndActivityChange() {
//        // Wait for 10 seconds to load up the cards
//        try {
//            Thread.sleep(10000); // 10 seconds in milliseconds
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        // Click on the ListView item
//        onView(withId(R.id.event_list)).perform(click());
//        // Check if the intent to start the EventDetailsActivity is sent
//        intended(hasComponent(new ComponentName(getTargetContext(), EventDetailsActivity.class)));
//
//        onView(withId(R.id.back_button)).perform(click());
//        intended(hasComponent(new ComponentName(getTargetContext(), OrganizerActivity.class)));
//    }
//

//    @Test
//    public void testEditButtonAndMessage() {
//        // Wait for 10 seconds to load up the cards
//        try {
//            Thread.sleep(10000); // 10 seconds in milliseconds
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        // Click on the ListView item
//        onView(withId(R.id.event_list)).perform(click());
//        // Check if the intent to start the EventDetailsActivity is sent
//        intended(hasComponent(new ComponentName(getTargetContext(), EventDetailsActivity.class)));
//
//        onView(withId(R.id.floatingEditButton)).perform(click());
//        intended(hasComponent(new ComponentName(getTargetContext(), EditEventDetails.class)));
//
//        onView(withId(R.id.create_message)).perform(click());
//        onView(withText("Create Message")).check(matches(isDisplayed()));
//        onView(allOf(withHint("Title:"), isAssignableFrom(EditText.class))).check(matches(isDisplayed()));
//        onView(allOf(withHint("Message:"), isAssignableFrom(EditText.class))).check(matches(isDisplayed()));
//
//        // Perform actions in the dialog
//        onView(allOf(withHint("Title:"), isAssignableFrom(EditText.class))).perform(typeText("Test Message Title"), closeSoftKeyboard());
//        onView(allOf(withHint("Message:"), isAssignableFrom(EditText.class))).perform(typeText("This is a test message body"), closeSoftKeyboard());
//
//        // Click on the OK button
//        onView(withText("OK")).perform(click());
//        // Verify if the pop-up notification gets displayed
//        intended(allOf(
//                hasAction(equalTo("android.intent.action.NOTIFICATION")),
//                hasExtra("notification_title", "Test Message Title"),
//                hasExtra("notification_body", "This is a test message body")
//        ));
//    }



//    // this test is good
//    @Test
//    public void testActivityChangeOnNotificationPress() {
//        // Click on the notifications button
//        onView(withId(R.id.notif_button)).perform(click());
//        // Check if the intent to start the NotificationsPage is sent
//        intended(hasComponent(new ComponentName(getTargetContext(), NotificationsPage.class)));
//    }
//
//    // this test is good
//
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