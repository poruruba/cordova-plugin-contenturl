package jp.or.sample.ContentUrlPlugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.content.ComponentName;

public class IntentReceiverActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent original = getIntent();

//        Intent safeIntent = new Intent(this, MainActivity.class);
        String packageName = getApplicationContext().getPackageName();
        Intent safeIntent = new Intent();
        safeIntent.setComponent(new ComponentName(packageName, packageName + ".MainActivity"));

        safeIntent.setAction(Intent.ACTION_SEND);
        safeIntent.setData(original.getData());
        safeIntent.setType(original.getType());
        safeIntent.putExtras(original.getExtras());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            safeIntent.setClipData(original.getClipData());
        }

        safeIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(safeIntent);
        finish();
    }
}

