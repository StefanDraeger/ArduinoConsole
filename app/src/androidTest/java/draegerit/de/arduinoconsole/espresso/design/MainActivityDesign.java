package draegerit.de.arduinoconsole.espresso.design;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Testet die Hautpseite {@link MainActivity} der Android Anwendung "ArduinoConsole" auf default Texte und Design.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityDesign {

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
    public void testSendBtnDefaultText() {
        onView(withId(R.id.sendBtn)).check(matches(withText(getResourceString(R.string.sendBtnTxt))));
    }

    /**
     * Prüft ob der {@link android.widget.Button} Senden im Defaultfall deaktiviert ist.
     */
    @Test
    public void testSendBtnDefaultDisabled() {
        onView(withId(R.id.sendBtn)).check(matches(not(isEnabled())));
    }

    /**
     * Prüft ob der {@link android.widget.Button} für das leeren der Konsole im Defaultfall aktiviert ist.
     */
    @Test
    public void testClearBtnDefaultEnabled() {
        onView(withId(R.id.clearBtn)).check(matches(isEnabled()));
    }

    /**
     * Prüft ob die {@link android.widget.TextView} welche die Konsole darstellt im Defaultfall leer ist.
     */
    @Test
    public void testKonsoleIsEmpty() {
        onView(withId(R.id.consoleTextView)).check(matches(withText("")));
    }

    /**
     * Prüft ob die {@link android.widget.CheckBox} für den Autoscroll die richtige Beschriftung trägt.
     */
    @Test
    public void testAutoscrollCheckboxText() {
        onView(withId(R.id.autoScrollCheckbox)).check(matches(withText(getResourceString(R.string.autoscrollChkBoxTxt))));
    }

    /**
     * Prüft ob die {@link android.widget.CheckBox} für den Autoscroll im Defaultfall deaktiviert ist.
     */
    @Test
    public void testAutoscrollCheckboxIsNotCheckted() {
        onView(withId(R.id.autoScrollCheckbox)).check(matches(not(isChecked())));
    }

    /**
     * Prüft ob der {@link android.widget.Button} für das einblenden / ausblenden der Schnittstellenkonfiguration
     * aktiviert ist.
     */
    @Test
    public void testConfigureBtnDefaultEnabled() {
        onView(withId(R.id.configureBtn)).check(matches(isEnabled()));
    }

    /**
     * Prüft ob die {@link android.widget.TableRow}s für die Schnittstellenkonfiguration im Defaultfall nicht sichtbar sind.
     */
    @Test
    public void testConfigureRowsDefaultVisibleGone() {
        onView(withId(R.id.config1TblRow)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.config2TblRow)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.config3TblRow)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }


    /**
     * Liefert einen String aus der Resourcedatei.
     *
     * @param id - ID der String Resource
     * @return String
     */
    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }

}
