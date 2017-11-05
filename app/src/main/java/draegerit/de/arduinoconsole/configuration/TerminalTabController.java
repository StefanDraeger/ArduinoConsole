package draegerit.de.arduinoconsole.configuration;


import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import draegerit.de.arduinoconsole.R;
import draegerit.de.arduinoconsole.util.PreferencesUtil;
import draegerit.de.arduinoconsole.util.configuration.TerminalConfiguration;

public class TerminalTabController extends AbstractTabController {

    private TerminalTab terminalTab;

    public TerminalTabController(TerminalTab terminalTab) {
        this.terminalTab = terminalTab;
    }

    public void registerListeners() {
        this.terminalTab.getDisplayTimestampCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                terminalTab.getTimestampSpinner().setEnabled(isChecked);
            }
        });

        this.terminalTab.getTimestampSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (view != null) {
                    String pattern = ((TextView) view).getText().toString();
                    setExampleTimestampText(pattern);
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });
    }

    public void save(Context ctx) {
        TerminalConfiguration terminalConfiguration = PreferencesUtil.getTerminalConfiguration(ctx);
        terminalConfiguration.setShowTimestampsBeforeMessageText(this.terminalTab.getDisplayTimestampCheckBox().isChecked());
        terminalConfiguration.setMessageDateFormat(this.terminalTab.getTimestampSpinner().getSelectedItem().toString());

        PreferencesUtil.storeTerminalConfiguration(ctx, terminalConfiguration);
        Toast.makeText(ctx, ctx.getString(R.string.save_terminal), Toast.LENGTH_LONG).show();
    }

    /**
     * Setzt die Default Werte.
     */
    public void setDefaultValues(Context ctx) {
        TerminalConfiguration terminalConfiguration = PreferencesUtil.getTerminalConfiguration(ctx);
        boolean isShowTimestamps = terminalConfiguration.isShowTimestampsBeforeMessageText();
        this.terminalTab.getDisplayTimestampCheckBox().setChecked(isShowTimestamps);
        this.terminalTab.getTimestampSpinner().setEnabled(isShowTimestamps);
        this.terminalTab.getTimestampSpinner().setSelection(getPositionForValue(this.terminalTab.getTimestampSpinner(), terminalConfiguration.getMessageDateFormat()));
        setExampleTimestampText(this.terminalTab.getTimestampSpinner().getSelectedItem().toString());
    }

    private void setExampleTimestampText(String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        String actualDate = dateFormat.format(new Date());
        terminalTab.getTimestampExampleTextView().setText(actualDate);
    }



}
