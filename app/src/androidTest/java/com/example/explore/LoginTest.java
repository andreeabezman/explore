package com.example.explore;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoginTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.explore", appContext.getPackageName());
    }

        /*
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule=
        new ActivityTestRule<>(MainActivity.class);

    @Test
    public void ensureOpened(){
        onView(withId(R.id.etUserLogin)).
            perform(typeText("madalina"),
                closeSoftKeyboard());
        onView(withId(R.id.etPasswordLogin)).
            perform(typeText("madalina"),
                closeSoftKeyboard());
        onView(withId(R.id.btLogin)).
            perform(click());;
        onView(withId(R.id.search_places)).
            check(matches(isDisplayed)));
        }

     */


}
