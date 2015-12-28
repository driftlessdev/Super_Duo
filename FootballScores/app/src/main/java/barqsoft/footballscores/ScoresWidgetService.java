package barqsoft.footballscores;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Tim on 12/28/2015.
 */
@SuppressLint("NewApi")
public class ScoresWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        ScoresWidgetDataProvider factory = new ScoresWidgetDataProvider(getApplicationContext());
        return factory;
    }
}
