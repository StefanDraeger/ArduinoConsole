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

public class TerminalTabController extends AbstractTabController<TerminalTab> {

    public TerminalTabController(TerminalTab tab) {
        super(tab);
    }

    public void registerListeners() {
        this.tab.getDisplayTimestampCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tab.getTimestampSpinner().setEnabled(isChecked);
            }
        });

        this.tab.getTimestampSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        terminalConfiguration.setShowTimestampsBeforeMessageText(this.tab.getDisplayTimestampCheckBox().isChecked());
        terminalConfiguration.setMessageDateFormat(this.tab.getTimestampSpinner().getSelectedItem().toString());

        PreferencesUtil.storeTerminalConfiguration(ctx, terminalConfiguration);
        Toast.makeText(ctx, ctx.getString(R.string.save_terminal), Toast.LENGTH_LONG).show();
    }

    /**
     * Setzt die Default Werte.
     */
    public void setDefaultValues(Context ctx) {
        TerminalConfiguration terminalConfiguration = PreferencesUtil.getTerminalConfiguration(ctx);
        boolean isShowTimestamps = terminalConfiguration.isShowTimestampsBeforeMessageText();
        this.tab.getDisplayTimestampCheckBox().setChecked(isShowTimestamps);
        this.tab.getTimestampSpinner().setEnabled(isShowTimestamps);
        this.tab.getTimestampSpinner().setSelection(getPositionForValue(this.tab.getTimestampSpinner(), terminalConfiguration.getMessageDateFormat()));
        setExampleTimestampText(this.tab.getTimestampSpinner().getSelectedItem().toString());
    }

    private void setExampleTimestampText(String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        String actualDate = dateFormat.format(new Date());
        tab.getTimestampExampleTextView().setText(actualDate);
    }



}
