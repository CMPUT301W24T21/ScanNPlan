package com.example.project_3;

import android.content.ComponentName;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.google.firebase.firestore.util.Assert.fail;
import static org.junit.Assert.assertTrue;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
    // this test is good
//    @Test
//    public void testActivityChangeOnNotificationPress() {
//        // Click on the notifications button
//        onView(withId(R.id.notif_button)).perform(click());
//        // Check if the intent to start the NotificationsPage is sent
//        intended(hasComponent(new ComponentName(getTargetContext(), NotificationsPage.class)));
//    }

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
        onView(withId(R.id.add_event_editText)).perform(typeText("Test Event"), closeSoftKeyboard());

        // Click on the OK button to confirm event addition
        onView(withText("OK")).perform(click());

        // Verify if the event appears in the window
        onView(withText("Test Event")).check(matches(isDisplayed()));
    }
}