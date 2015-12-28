package barqsoft.footballscores;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tim on 12/28/2015.
 */
@SuppressLint("NewApi")
public class ScoresWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Cursor mCursor;
    private ContentResolver mResolver;

    public ScoresWidgetDataProvider(Context context)
    {
        mContext = context;
        mResolver = mContext.getContentResolver();
    }

    @Override
    public void onCreate() {


    }

    @Override
    public void onDataSetChanged() {
        if(mCursor != null)
        {
            mCursor.close();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date(System.currentTimeMillis());
        mCursor = mResolver.query(
                DatabaseContract.scores_table.buildScoreWithDate()
                ,null
                ,null
                ,new String[]{dateFormat.format(today)}
                ,null);
    }

    @Override
    public void onDestroy() {
        if(mCursor != null)
        {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        if(mCursor != null)
            return mCursor.getCount();
        else
            return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if(mCursor == null)
        {
            return null;
        }
        mCursor.moveToPosition(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(),R.layout.scores_list_item);
        views.setTextViewText(
                R.id.home_name,
                mCursor.getString(scoresAdapter.COL_HOME));
        views.setTextViewText(
                R.id.away_name,
                mCursor.getString(scoresAdapter.COL_AWAY));
        views.setTextViewText(
                R.id.score_textview,
                Utilies.getScores(
                        mCursor.getInt(scoresAdapter.COL_HOME_GOALS),
                        mCursor.getInt(scoresAdapter.COL_AWAY_GOALS),
                        mContext));
        views.setImageViewResource(
                R.id.home_crest,
                Utilies.getTeamCrestByTeamName(mCursor.getString(scoresAdapter.COL_HOME))
        );
        views.setImageViewResource(
                R.id.away_crest,
                Utilies.getTeamCrestByTeamName(mCursor.getString(scoresAdapter.COL_AWAY))
        );
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if(mCursor != null)
        {
            return mCursor.getInt(scoresAdapter.COL_ID);
        }
        else
        {
            return position;
        }
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
