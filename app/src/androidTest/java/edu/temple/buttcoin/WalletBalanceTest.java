package edu.temple.buttcoin;


import android.app.Instrumentation;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicReference;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class WalletBalanceTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup(){
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        BitcoinDbHelper dbHelper = new BitcoinDbHelper(instrumentation.getTargetContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(db, 0, 0);
    }

    @Test
    public void testBalanceFragmentFetchAndSave() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Ir"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(withId(R.id.activity_main)))),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.text1), withText("Ver Balance de Wallet"),
                        childAtPosition(
                                allOf(withId(R.id.drawer),
                                        withParent(withId(R.id.drawerLayout))),
                                1),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.walletSelector), isDisplayed()));
        appCompatSpinner.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(withText(containsString("Obtener")), isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.walletInput), isDisplayed()));
        appCompatEditText.perform(replaceText("1F1tAaz5x1HUXrCNLbtMDqcw6o5GNn4xqX"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.getWalletButton), withText("Ir"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.popFromStackButton), withText("Regresar"), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatSpinner2 = onView(
                allOf(withId(R.id.walletSelector), isDisplayed()));
        appCompatSpinner2.perform(click());

        ViewInteraction linearLayout2 = onView(
                allOf(withText(containsString("1F1tAaz5x1HUXrCNLbtMDqcw6o5GNn4xqX")), isDisplayed()));
        //Will throw NoMatchingViewException if our wallet wasn't stored
        linearLayout2.perform(click());


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
