package draegerit.de.arduinoconsole.configuration;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import draegerit.de.arduinoconsole.R;


public class ConnectionTab extends Fragment {

    /**
     * Auswahlliste für die Auswahl der Übertragungsgeschwindigkeit.
     **/
    private Spinner deviceBaudSpinner;

    /**
     * Auswahlliste für die Auswahl der Databits.
     **/
    private Spinner databitSpinner;

    /**
     * Auswahlliste für die Auswahl der Stopbits.
     **/
    private Spinner stopbitsSpinner;

    /**
     * Auswahlliste für die Auswahl der Parity.
     **/
    private Spinner paritySpinner;

    /**
     * Checkbox für das Anzeigen des Dialoges zum schließen einer nicht benötigten Bluetoothverbindung.
     */
    private CheckBox closeBluetoothConnectionCheckbox;

    /**
     * Checkbox für das Anzeigen des Dialoges zum suchen neuer Geräte.
     */
    private CheckBox showSearchNewDevicesChkBox;

    private CheckBox sendLineBreaksChkBox;

    private CheckBox sendGreetingsChkBox;

    private EditText greetingsMessageText;

    private View view;

    private ConnectionTabController connectionTabController;

    private ImageView serialToggleImage;
    private TextView serialTitelText;
    private LinearLayout[] serialRows;

    private ImageView btToggleImage;
    private TextView btTitelText;
    private LinearLayout[] btRows;

    private ImageView httpToggleImage;
    private TextView httpTitelText;
    private LinearLayout[] httpRows;

    private Spinner httpProfileSpinner;
    private ImageButton addProfileBtn;
    private ImageButton removeProfileBtn;
    private ImageButton editProfileBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.configuration_tab_connection, container, false);
        registerComponents();
        this.connectionTabController = new ConnectionTabController(this);
        this.connectionTabController.setDefaultValues(getContext());
        return this.view;
    }

    /**
     * Erzeugt die Komponenten der View.
     */
    private void registerComponents() {
        this.deviceBaudSpinner = (Spinner) this.view.findViewById(R.id.baudSpinner);
        this.databitSpinner = (Spinner) this.view.findViewById(R.id.databitSpinner);
        this.stopbitsSpinner = (Spinner) this.view.findViewById(R.id.stopbitsSpinner);
        this.paritySpinner = (Spinner) this.view.findViewById(R.id.paritySpinner);

        this.sendLineBreaksChkBox = (CheckBox) this.view.findViewById(R.id.sendLineBreaksChkBox);
        this.closeBluetoothConnectionCheckbox = (CheckBox) this.view.findViewById(R.id.closeBluetoothConnectionCheckbox);
        this.showSearchNewDevicesChkBox = (CheckBox) this.view.findViewById(R.id.showSearchNewDevicesChkBox);
        this.sendGreetingsChkBox = (CheckBox) this.view.findViewById(R.id.sendGreetingsChkBox);
        this.greetingsMessageText = (EditText) this.view.findViewById(R.id.greetingsMessageText);

        this.serialToggleImage = (ImageView) this.view.findViewById(R.id.serialToggleImage);
        this.serialTitelText = (TextView) this.view.findViewById(R.id.serialTitelText);
        LinearLayout serialRow1 = (LinearLayout) this.view.findViewById(R.id.serialRow1);
        LinearLayout serialRow2 = (LinearLayout) this.view.findViewById(R.id.serialRow2);
        LinearLayout serialRow3 = (LinearLayout) this.view.findViewById(R.id.serialRow3);
        LinearLayout serialRow4 = (LinearLayout) this.view.findViewById(R.id.serialRow4);
        this.serialRows = new LinearLayout[]{serialRow1, serialRow2, serialRow3, serialRow4};

        this.btToggleImage = (ImageView) this.view.findViewById(R.id.btToggleImage);
        this.btTitelText = (TextView) this.view.findViewById(R.id.btTitelText);
        LinearLayout btRow1 = (LinearLayout) this.view.findViewById(R.id.btRow1);
        LinearLayout btRow2 = (LinearLayout) this.view.findViewById(R.id.btRow2);
        this.btRows = new LinearLayout[]{btRow1, btRow2};


        this.httpToggleImage = (ImageView) this.view.findViewById(R.id.httpToggleImage);
        this.httpTitelText = (TextView) this.view.findViewById(R.id.httpTitelText);
        LinearLayout httpRow1 = (LinearLayout) this.view.findViewById(R.id.httpRow1);
        LinearLayout httpRow2 = (LinearLayout) this.view.findViewById(R.id.httpRow2);
        this.httpRows = new LinearLayout[]{httpRow1, httpRow2};
        this.httpProfileSpinner = (Spinner) this.view.findViewById(R.id.httpProfileSpinner);

        this.addProfileBtn = (ImageButton) this.view.findViewById(R.id.addProfileBtn);
        this.removeProfileBtn = (ImageButton) this.view.findViewById(R.id.removeProfileBtn);
        this.editProfileBtn = (ImageButton) this.view.findViewById(R.id.editProfileBtn);
    }

    public void save() {
        this.connectionTabController.save(getContext());
    }

    /**
     * Liefert die Auswahlliste {@link Spinner} für die Auswahl der Übertragungsgeschwindigkeit.
     *
     * @return {@link Spinner}
     */
    public Spinner getDeviceBaudSpinner() {
        return deviceBaudSpinner;
    }

    /**
     * Liefert die Auswahlliste {@link Spinner} für die Databits.
     *
     * @return {@link Spinner}
     */
    public Spinner getDatabitSpinner() {
        return databitSpinner;
    }

    /**
     * Liefert die Auswahlliste {@link Spinner} für die Stopbits.
     *
     * @return {@link Spinner}
     */
    public Spinner getStopbitsSpinner() {
        return stopbitsSpinner;
    }

    /**
     * Liefert die Auswahlliste {@link Spinner} für die Parity.
     *
     * @return {@link Spinner}
     */
    public Spinner getParitySpinner() {
        return paritySpinner;
    }

    public CheckBox getCloseBluetoothConnectionCheckbox() {
        return closeBluetoothConnectionCheckbox;
    }

    public void setCloseBluetoothConnectionCheckbox(CheckBox closeBluetoothConnectionCheckbox) {
        this.closeBluetoothConnectionCheckbox = closeBluetoothConnectionCheckbox;
    }

    public CheckBox getShowSearchNewDevicesChkBox() {
        return showSearchNewDevicesChkBox;
    }

    public void setShowSearchNewDevicesChkBox(CheckBox showSearchNewDevicesChkBox) {
        this.showSearchNewDevicesChkBox = showSearchNewDevicesChkBox;
    }

    public CheckBox getSendGreetingsChkBox() {
        return sendGreetingsChkBox;
    }

    public EditText getGreetingsMessageText() {
        return greetingsMessageText;
    }

    public CheckBox getSendLineBreaksChkBox() {
        return sendLineBreaksChkBox;
    }

    public ImageView getSerialToggleImage() {
        return serialToggleImage;
    }

    public TextView getSerialTitelText() {
        return serialTitelText;
    }

    public ImageView getBtToggleImage() {
        return btToggleImage;
    }

    public LinearLayout[] getSerialRows() {
        return serialRows;
    }

    public TextView getBtTitelText() {
        return btTitelText;
    }

    public LinearLayout[] getBtRows() {
        return btRows;
    }

    public ImageView getHttpToggleImage() {
        return httpToggleImage;
    }

    public TextView getHttpTitelText() {
        return httpTitelText;
    }

    public LinearLayout[] getHttpRows() {
        return httpRows;
    }

    public Spinner getHttpProfileSpinner() {
        return httpProfileSpinner;
    }

    public ImageButton getAddProfileBtn() {
        return addProfileBtn;
    }

    public ImageButton getRemoveProfileBtn() {
        return removeProfileBtn;
    }

    public ImageButton getEditProfileBtn() {
        return editProfileBtn;
    }
}
