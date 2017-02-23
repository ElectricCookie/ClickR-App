package se.kth.clickr;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.LinearLayout;

import trikita.anvil.BaseDSL;
import trikita.anvil.DSL;
import trikita.anvil.RenderableView;

/**
 * Created by ElectricCookie on 25.08.2016.
 */
public class LoginView extends RenderableView{

    public LoginView(Context c){
        super(c);
    }

    @Override
    public void view() {


        State state = App.getInstance().getStore().getState();

        DSL.linearLayout(() -> {
            DSL.size(DSL.MATCH,DSL.WRAP);
            DSL.padding(30);
            DSL.orientation(LinearLayout.VERTICAL);
            DSL.textView(() -> {
                DSL.text(R.string.login_text_welcome);
                DSL.size(DSL.MATCH,DSL.WRAP);
            });

            DSL.textView(() -> {
                if(!state.login().usernameValid()) {
                    DSL.text(getContext().getString(state.login().usernameError()));
                }
            });

            DSL.editText(() -> {
                DSL.hint(R.string.login_prompt_username);
                DSL.text(state.login().username());
                DSL.inputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);
                DSL.onTextChanged(new BaseDSL.SimpleTextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s) {
                        App.getInstance().getStore().dispatch(new Action("LOGIN_SET_USERNAME",s.toString()));
                    }
                });
            });

            DSL.editText(() -> {
                DSL.hint(R.string.login_prompt_password);
                DSL.text(state.login().password());
                DSL.inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                DSL.onTextChanged(new BaseDSL.SimpleTextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s) {
                        App.getInstance().getStore().dispatch(new Action("LOGIN_SET_PASSWORD",s.toString()));
                    }
                });
            });

            DSL.button(() -> {
                DSL.text(R.string.login_action_login);
                DSL.onClick(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        App.getInstance().getStore().dispatch(new Action("LOGIN_SUBMIT",getContext()));
                    }
                });
            });
        });

    }
}
