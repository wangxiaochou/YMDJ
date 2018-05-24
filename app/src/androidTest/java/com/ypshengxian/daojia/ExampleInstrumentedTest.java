package com.ypshengxian.daojia;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.ypshengxian.daojia.ui.activity.BindingPhoneActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.ypshengxian.daojia", appContext.getPackageName());
    }



    @Test
    public void UserDataActivity() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        Intent intent = new Intent(context, BindingPhoneActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        SystemClock.sleep(TimeUnit.HOURS.toMillis(1));
    }
}
