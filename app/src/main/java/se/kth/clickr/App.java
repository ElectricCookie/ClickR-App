package se.kth.clickr;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.liulishuo.filedownloader.FileDownloader;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import se.kth.clickr.middleware.AccountMiddleware;
import se.kth.clickr.middleware.ApiMiddleware;
import se.kth.clickr.middleware.ConnectMiddleware;
import se.kth.clickr.middleware.LogMiddleware;
import se.kth.clickr.middleware.LoginMiddleware;
import se.kth.clickr.middleware.PlaybackMiddleware;
import se.kth.clickr.middleware.ScenarioSessionsMiddleware;
import se.kth.clickr.middleware.TestScenariosMiddleware;
import se.kth.clickr.middleware.TracksMiddleware;
import se.kth.clickr.reducers.AccountReducer;
import se.kth.clickr.reducers.ConnectReducer;
import se.kth.clickr.reducers.HomeReducer;
import se.kth.clickr.reducers.LifecycleReduer;
import se.kth.clickr.reducers.LoginReducer;
import se.kth.clickr.reducers.PlaybackReducer;
import se.kth.clickr.reducers.ScenarioSessionsReducer;
import se.kth.clickr.reducers.TestScenarioReducer;
import se.kth.clickr.reducers.TracksReducer;
import trikita.anvil.Anvil;

/**
 * Created by ElectricCookie on 24.08.2016.
 */
public class App extends Application{

    private Store<Action, State> store = null;

    private static App sInstance;



    @Override
    public void onCreate() {
        super.onCreate();


        FileDownloader.init(getBaseContext());


        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

        });

        sInstance = this;


        if(store == null){

            store = new Store<Action, State>(State.getDefault(this));

            store.registerMiddleware(new LogMiddleware());
            store.registerMiddleware(new ApiMiddleware(this));
            store.registerMiddleware(new AccountMiddleware());
            store.registerMiddleware(new ConnectMiddleware());
            store.registerMiddleware(new LoginMiddleware());
            store.registerMiddleware(new TestScenariosMiddleware());
            store.registerMiddleware(new TracksMiddleware());
            store.registerMiddleware(new ScenarioSessionsMiddleware());
            store.registerMiddleware(new PlaybackMiddleware());


            store.registerReducer(new ConnectReducer());
            store.registerReducer(new LoginReducer());
            store.registerReducer(new HomeReducer());
            store.registerReducer(new AccountReducer());
            store.registerReducer(new LifecycleReduer());
            store.registerReducer(new TestScenarioReducer());
            store.registerReducer(new ScenarioSessionsReducer());
            store.registerReducer(new TracksReducer());
            store.registerReducer(new PlaybackReducer());


            this.store.subscribe(new Store.Subscriber<State>() {
                @Override
                public boolean shouldUpdate(State oldState, State newState) {
                    return true;
                }

                @Override
                public void update(State newState) {
                    Anvil.render();
                }
            });

        }


    }

    public static App getInstance(){
        return sInstance;
    }

    public Store<Action, State> getStore() {
        return store;
    }




}
