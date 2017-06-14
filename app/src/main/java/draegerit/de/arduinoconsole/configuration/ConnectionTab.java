package draegerit.de.arduinoconsole.configuration;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

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

    private View view;

    private ConnectionTabController connectionTabController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.configuration_tab_connection, container, false);
        this.connectionTabController = new ConnectionTabController(this);
        registerComponents();
        this.connectionTabController.registerListeners();
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

}
