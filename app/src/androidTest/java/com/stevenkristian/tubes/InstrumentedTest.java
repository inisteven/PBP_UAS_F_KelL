package com.stevenkristian.tubes;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void instrumentedTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.username_et),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.username_til),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText.perform(replaceText("admin@admin.com"), closeSoftKeyboard());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.password_et),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.password_til),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText2.perform(replaceText("admin"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.login_btn), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                2)));
        materialButton.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction cardView = onView(
                allOf(withId(R.id.motor),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0)));
        cardView.perform(scrollTo(), click());

        ViewInteraction cardView2 = onView(
                allOf(withId(R.id.cardMotor),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.frame_view_motor_admin),
                                        0),
                        0)));
        cardView2.perform(swipeLeft());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.ivHapus), withText("Hapus"),
                        childAtPosition(
                                allOf(withId(R.id.right),
                                        childAtPosition(
                                                withClassName(is("com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(android.R.id.button2), withText("No"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));
        materialButton2.perform(scrollTo(), click());

        ViewInteraction cardView3 = onView(
                allOf(withId(R.id.cardMotor),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.frame_view_motor_admin),
                                        0),
                                0)));
        cardView3.perform(swipeLeft());

        ViewInteraction materialTextView2 = onView(
                allOf(withId(R.id.ivHapus), withText("Hapus"),
                        childAtPosition(
                                allOf(withId(R.id.right),
                                        childAtPosition(
                                                withClassName(is("com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        materialTextView2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(android.R.id.button1), withText("Yes"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton3.perform(scrollTo(), click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
