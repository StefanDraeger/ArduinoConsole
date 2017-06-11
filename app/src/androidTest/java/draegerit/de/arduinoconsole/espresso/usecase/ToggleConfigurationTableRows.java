package draegerit.de.arduinoconsole.espresso.usecase;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import draegerit.de.arduinoconsole.MainActivity;
import draegerit.de.arduinoconsole.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ToggleConfigurationTableRows {

    /**
     * Die {@link ActivityTestRule} wird instanziiert bevor eine Methode
     *
     * @Before gestartet wird und wird nach der Methode @After wieder entfernt.
     */
    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Prüft ob der {@link android.widget.Button} Senden die richtige Beschriftung trägt.
     */
    @Test
    public void testTableRowsToggleVisibility() {
        onView(withId(R.id.configureBtn)).perform(click());
        onView(withId(R.id.config1TblRow)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    //    onView(withId(R.id.config2TblRow)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.config3TblRow)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withId(R.id.configureBtn)).perform(click());
        onView(withId(R.id.config1TblRow)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    //    onView(withId(R.id.config2TblRow)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.config3TblRow)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }
}
