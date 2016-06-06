package com.firebasedemo.nanochat;

import org.junit.Test;

import static org.junit.Assert.*;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.AutoCompleteTextView;
import android.widget.*;
import android.app.*;
import android.test.TouchUtils;
import android.app.Instrumentation.*;

import com.firebasedemo.nanochat.FileMsg;
import com.firebasedemo.nanochat.LoginActivity;
import com.firebasedemo.nanochat.d_homeActivity;

import java.lang.String;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class MainActivityTests extends ActivityInstrumentationTestCase2<LoginActivity>{
    /*@Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }*/
    public MainActivityTests() {
        super(LoginActivity.class);
    }

    public void testActivityExists() {
       LoginActivity activity = getActivity();

        assertNotNull(activity);
    }

    public void testGreet() {
        LoginActivity activity = getActivity();
        ActivityMonitor activityMonitor = getInstrumentation().addMonitor(d_homeActivity.class.getName(), null, false);
        final AutoCompleteTextView nameEditText =
                (AutoCompleteTextView) activity.findViewById(R.id.email);

        // Send string input value
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                nameEditText.requestFocus();
            }
        });

        getInstrumentation().waitForIdleSync();
        getInstrumentation().sendStringSync("Jake");

        Button greetButton =
                (Button) activity.findViewById(R.id.email_sign_in_button);

        TouchUtils.clickView(this, greetButton);

        /*d_homeActivity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        nextActivity .finish();*/


        d_homeActivity targetActivity = (d_homeActivity) activityMonitor.waitForActivity(); // By using ActivityMonitor
        // TargetActivity targetActivity = (TargetActivity) activityMonitor.waitForActivityWithTimeout(5);// It also works
        // TargetActivity targetActivity = (TargetActivity) getInstrumentation().waitForMonitor(activityMonitor); // By using Instrumentation
        // TargetActivity targetActivity = (TargetActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5); // It also works
        assertNotNull("Target Activity is not launched", targetActivity);
    }

    public void testFileMsg(){
        String bla="Test";
        String value="value";
        FileMsg file=new FileMsg(bla,bla,bla,bla,value,bla);
        assertEquals("Tested msg",bla,file.getText());

    }

    public void testFileName(){
        String bla="Test";
        String value="value";
        FileMsg file=new FileMsg(value,bla,bla,bla,bla,bla);
        assertEquals("Tested msg",value,file.getMsg());
        //fail();

    }
}