package draegerit.de.arduinoconsole;

import android.view.View;

public class GraphController extends AbstractController {


    private GraphActivity graphActivity;

    public GraphController(GraphActivity graphActivity) {
        super(graphActivity);
        this.graphActivity = graphActivity;
    }


    @Override
    public void registerComponents() {
        this.graphActivity.getGraphClearBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphActivity.getChartView().getDataset().getSeries(0).clear();
            }
        });

        this.graphActivity.getPrefBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphActivity.showChartPreferences();
            }
        });
    }

}
