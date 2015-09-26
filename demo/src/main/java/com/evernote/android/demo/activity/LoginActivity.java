package com.evernote.android.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.evernote.android.demo.R;
import com.evernote.client.android.EvernoteSession;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.User;
import com.evernote.thrift.TException;

/**
 * @author rwondratschek
 */
public class LoginActivity extends AppCompatActivity {

    public static void launch(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tb_text));

        setSupportActionBar(toolbar);

        mButton = (Button) findViewById(R.id.button_login);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EvernoteSession.getInstance().authenticate(LoginActivity.this);
                mButton.setEnabled(false);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EvernoteSession.REQUEST_CODE_LOGIN:
                EvernoteSession.getInstance().finishAuthorization(this, resultCode, data);
                getUserInfo();
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public void getUserInfo() {
        try {
            User user = EvernoteSession.getInstance().getEvernoteClientFactory().getUserStoreClient().getUser();
            String name = user.getName();
            String email = user.getEmail();
        } catch (EDAMUserException e) {
            e.printStackTrace();
        } catch (EDAMSystemException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }
}
