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

    }

}
