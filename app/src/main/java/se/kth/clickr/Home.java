package se.kth.clickr;

import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

import se.kth.clickr.flood.Endpoints;

public class Home extends ApiBoundActivity {

    private Dialog dialog;
    private boolean initial = true;


    private enum HOME_FRAGMENTS{
        TEST_SCENARIOS,
        SCENARIO_SESSIONS
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ProfileDrawerItem profileDrawerItem = new ProfileDrawerItem();

        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(profileDrawerItem)
                .withHeaderBackground(R.drawable.cover)
                .build();

        new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(header)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                        .withName(R.string.open_scenarios)
                        .withIcon(R.drawable.ic_dashboard)
                        .withOnDrawerItemClickListener((view, pos, item) -> {
                            showFragment(HOME_FRAGMENTS.TEST_SCENARIOS);
                            return false;
                        }),
                        new PrimaryDrawerItem()
                        .withName(R.string.scenario_sessions)
                        .withIcon(R.drawable.ic_touch_app)
                        .withOnDrawerItemClickListener((view, pos, item) -> {
                            showFragment(HOME_FRAGMENTS.SCENARIO_SESSIONS);
                            return false;
                        })
                )
                .build();


        App.getInstance().getStore().subscribe(new Store.Subscriber<State>() {
            @Override
            public boolean shouldUpdate(State oldState, State newState) {
                return oldState.account() != newState.account();
            }

            @Override
            public void update(State newState) {
                runOnUiThread(() -> {
                    Endpoints.UserProfile account = newState.account();
                    if(account != null){

                        profileDrawerItem
                                .withEmail(account.email())
                                .withName(account.fullName())
                                .withIcon(account.avatar());

                        header.updateProfile(profileDrawerItem);

                    }


                });
            }
        });


        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.test_scenarios_join_title)
                .setMessage(R.string.test_scenarios_join_message)
                .setPositiveButton(R.string.test_scenarios_action_join, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getInstance().getStore().dispatch(new Action("TEST_SCENARIOS_JOIN", Home.this));
                    }
                })
                .setNegativeButton(R.string.test_scenarios_action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getInstance().getStore().dispatch(new Action("TEST_SCENARIOS_HIDE_DIALOG", null));
                    }
                })
                .create();


        App.getInstance().getStore().subscribe(new Store.Subscriber<State>() {
            @Override
            public boolean shouldUpdate(State oldState, State newState) {
                return true;
            }

            @Override
            public void update(State newState) {
                updateDialogVisibility();
            }
        });

        showFragment(HOME_FRAGMENTS.TEST_SCENARIOS);

    }


    public void showFragment(HOME_FRAGMENTS fragment){

        Fragment chosenFragment = null;

        switch (fragment){

            case TEST_SCENARIOS:

                chosenFragment = new TestScenariosView();

                break;

            case SCENARIO_SESSIONS:

                chosenFragment = new ScenarioSessionsView();

                break;
        }

        if(chosenFragment != null){
            getFragmentManager().beginTransaction().replace(R.id.content_home,chosenFragment).commitAllowingStateLoss();
        }



    }

    public void updateDialogVisibility() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (App.getInstance().getStore().getState().testScenarios().showJoinDialog()) {
                    dialog.show();
                } else {
                    dialog.hide();
                }

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        dialog.hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getInstance().getStore().dispatch(new Action("ACCOUNT_REQUEST_LOAD", null));
        updateDialogVisibility();
    }

    @Override
    public void onEvent(Action action, State state) {
        switch (action.getType()){
            case "PLAYBACK_SHOW":

                startActivity(new Intent(this,PlaybackActivity.class));

                break;
        }
    }
}
