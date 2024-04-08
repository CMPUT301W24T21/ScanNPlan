package com.example.project_3;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.Predicates.instanceOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * JUnit test class for testing AttendeeActivity functionality.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AttendeeActivityTest {

    /**
     * Setup method to launch the AttendeeActivity and perform initial actions.
     */
    @Before
    public void setup() {
        ActivityScenario<AttendeeActivity> scenario = ActivityScenario.launch(AttendeeActivity.class);
        onView(withId(R.id.BrowseEventsButton)).perform(click());
        scenario.onActivity(activity -> {
            ListView eventListView = activity.findViewById(R.id.event_list);
            //eventListView.performItemClick(null, 0, null);
        });
    }

    /**
     * Test case to check if the event list is initially empty.
     */
    @Test
    public void testEventListNotEmpty() {
        ActivityScenario<AttendeeActivity> scenario = ActivityScenario.launch(AttendeeActivity.class);
        scenario.onActivity(activity -> {
            ListView eventListView = activity.findViewById(R.id.event_listView);
            assertThat(eventListView.getCount(), greaterThan(0)); // Assuming initially the list is empty
        });
    }

    /**
     * Test case to check the functionality of the browse events button.
     */
    @Test
    public void testBrowseEventsButton() {
        ActivityScenario<AttendeeActivity> scenario = ActivityScenario.launch(AttendeeActivity.class);
        scenario.onActivity(activity -> {
            FloatingActionButton browseEventsButton = activity.findViewById(R.id.BrowseEventsButton);
            browseEventsButton.performClick();

            Intent expectedIntent = new Intent(activity, AttendeeBrowseEventsActivity.class);
            Intent actualIntent = getAndAssertThatStartedActivityIsNotNull(expectedIntent);
            assertThat(actualIntent.getComponent(), equalTo(expectedIntent.getComponent()));
        });
    }

    /**
     * Test case to check the functionality of the edit profile button.
     */
    @Test
    public void testEditProfileButton() {
        ActivityScenario<AttendeeActivity> scenario = ActivityScenario.launch(AttendeeActivity.class);
        scenario.onActivity(activity -> {
            FloatingActionButton editProfileButton = activity.findViewById(R.id.EditProfile);
            editProfileButton.performClick();

            // Start the AttendeeBrowseEventsActivity
            Intent expectedIntent = new Intent(activity, AttendeeBrowseEventsActivity.class);
            Intent actualIntent = getAndAssertThatStartedActivityIsNotNull(expectedIntent);
            assertThat(actualIntent.getComponent(), equalTo(expectedIntent.getComponent()));
        });
    }

    /**
     * Test case to check the functionality of the profile settings button.
     */
    @Test
    public void testProfileSettingsButton() {
        ActivityScenario<AttendeeActivity> scenario = ActivityScenario.launch(AttendeeActivity.class);
        scenario.onActivity(activity -> {
            FloatingActionButton profileSettingsButton = activity.findViewById(R.id.EditProfile);
            profileSettingsButton.performClick();

            // Verify that the profile settings fragment is displayed
            Fragment profileSettingsFragment = activity.getSupportFragmentManager().findFragmentById(R.id.attendee_fragment_container);
            if (profileSettingsFragment != null) {
                // Fragment is displayed
                Log.d("Test", "Profile settings fragment is displayed");
            } else {
                // Fragment is not displayed
                Log.d("Test", "Profile settings fragment is not displayed");
            }
        });
    }

    @Test
    public void testFirebaseProfileInformationMatches() {
        ActivityScenario<AttendeeActivity> scenario = ActivityScenario.launch(AttendeeActivity.class);
        scenario.onActivity(activity -> {
            TextInputEditText profileNameEditText = activity.findViewById(R.id.profile_name_editText);
            TextInputEditText homepageEditText = activity.findViewById(R.id.homepage_editText);
            TextInputEditText contactInfoEditText = activity.findViewById(R.id.contact_info_editText);

            if (profileNameEditText != null && homepageEditText != null && contactInfoEditText != null) {
                // Save the current information
                String originalProfileName = profileNameEditText.getText().toString();
                String originalHomepage = homepageEditText.getText().toString();
                String originalContactInfo = contactInfoEditText.getText().toString();

                // Erase it
                profileNameEditText.setText("");
                homepageEditText.setText("");
                contactInfoEditText.setText("");

                // Log message
                Log.d("ProfileTest", "Information erased");

                // Restore the original information
                profileNameEditText.setText(originalProfileName);
                homepageEditText.setText(originalHomepage);
                contactInfoEditText.setText(originalContactInfo);

                // Print message
                Log.d("ProfileTest", "Information restored");

                // Check if the information is still there
                if (originalProfileName.equals(profileNameEditText.getText().toString())
                        && originalHomepage.equals(homepageEditText.getText().toString())
                        && originalContactInfo.equals(contactInfoEditText.getText().toString())) {
                    Log.d("ProfileTest", "Information matches after navigation");
                } else {
                    Log.e("ProfileTest", "Information does not match after navigation");
                }
            } else {
                Log.e("ProfileTest", "One or more EditTexts are null");
            }
        });
    }

//    @Test
//    public void testFirebaseDataMatchesDisplayedDataBrowseEvents() {
//        ActivityScenario<AttendeeActivity> scenario = ActivityScenario.launch(AttendeeActivity.class);
//        scenario.onActivity(activity -> {
//            // Navigate to the Browse Events page
//            onView(withId(R.id.BrowseEventsButton)).perform(click());
//
//            // Get the count of items displayed on the Browse Events page
//            ListView eventListView = activity.findViewById(R.id.event_list);
//            int displayedItemCount = eventListView.getCount();
//
//            // Close the current activity to prevent interference with other tests
//            scenario.close();
//
//            // Perform Firebase operations in a new ActivityScenario
//            ActivityScenario<AttendeeActivity> firebaseScenario = ActivityScenario.launch(AttendeeActivity.class);
//            firebaseScenario.onActivity(firebaseActivity -> {
//                // Assuming you have a Firebase database reference
//                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("your_firebase_path");
//
//                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        // Get the count of items in Firebase
//                        long firebaseItemCount = dataSnapshot.getChildrenCount();
//
//                        // Compare the counts
//                        if (firebaseItemCount == displayedItemCount) {
//                            Log.d("FirebaseTest", "Counts match: " + firebaseItemCount);
//                        } else {
//                            Log.e("FirebaseTest", "Counts do not match. Firebase count: " + firebaseItemCount + ", Displayed count: " + displayedItemCount);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Log.e("FirebaseTest", "Firebase data fetch failed: " + databaseError.getMessage());
//                    }
//                });
//            });
//
//            // Close the Firebase scenario
//            firebaseScenario.close();
//        });
//    }




    @Test
    public void z_testFirebaseDataMatchesDisplayedData() {
        ActivityScenario<AttendeeActivity> scenario = ActivityScenario.launch(AttendeeActivity.class);
        scenario.onActivity(activity -> {
            // Assuming you have a Firebase database reference
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("your_firebase_path");

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Get the count of items in Firebase
                    long firebaseItemCount = dataSnapshot.getChildrenCount();

                    // Navigate to the Browse Events page
                    onView(withId(R.id.BrowseEventsButton)).perform(click());

                    // Get the count of items displayed on the Browse Events page
                    ListView eventListView = activity.findViewById(R.id.event_list);
                    int displayedItemCount = eventListView.getCount();

                    // Compare the counts
                    if (firebaseItemCount == displayedItemCount) {
                        Log.d("FirebaseTest", "Counts match: " + firebaseItemCount);
                    } else {
                        Log.e("FirebaseTest", "Counts do not match. Firebase count: " + firebaseItemCount + ", Displayed count: " + displayedItemCount);
                    }

                    // Navigate back to the previous page
                    pressBack();

                    // Verify that we are back on the original page
                    onView(withId(R.id.BrowseEventsButton)).check(matches(isDisplayed()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FirebaseTest", "Firebase data fetch failed: " + databaseError.getMessage());
                }
            });
        });
    }






    /**
     * Utility method to get the started activity and assert that it is not null.
     *
     * @param expectedIntent The expected intent of the started activity.
     * @return The actual intent of the started activity.
     */
    private Intent getAndAssertThatStartedActivityIsNotNull(Intent expectedIntent) {
        Instrumentation.ActivityResult activityResult = new Instrumentation.ActivityResult(Activity.RESULT_OK, expectedIntent);
        Instrumentation.ActivityMonitor monitor = InstrumentationRegistry.getInstrumentation()
                .addMonitor(AttendeeBrowseEventsActivity.class.getName(), activityResult, true);

        assertThat(monitor.getResult(), notNullValue());
        return new Intent(monitor.getResult().getResultData());
    }

    // Add more tests as needed
}
