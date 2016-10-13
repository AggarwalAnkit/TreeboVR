package com.example.aa.daydream;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class PanoramaActivity extends AppCompatActivity {

    VrPanoramaView panoWidgetView;
    private static String TAG = PanoramaActivity.class.getCanonicalName();
    private ImageLoaderTask backgroundImageLoaderTask;
    private String folderName = "office";
    private String fileName = "office_tech_floor.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panorama);

        panoWidgetView = (VrPanoramaView) findViewById(R.id.pano_view);

        panoWidgetView.setEventListener(new ActivityEventListener());

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        panoWidgetView.resumeRendering();
    }

    @Override
    protected void onPause() {
        panoWidgetView.pauseRendering();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        panoWidgetView.shutdown();

        if (backgroundImageLoaderTask != null) {
            backgroundImageLoaderTask.cancel(true);
        }
        super.onDestroy();
    }

    private void handleIntent(Intent intent) {
        // Determine if the Intent contains a file to load.
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            folderName = bundle.getString("folderName", "office");
            fileName = bundle.getString("fileName", "office_tech_floor.jpg");

            if (backgroundImageLoaderTask != null) {
                backgroundImageLoaderTask.cancel(true);
            }
            backgroundImageLoaderTask = new ImageLoaderTask();
            backgroundImageLoaderTask.execute(Pair.create(folderName, fileName));
        }
    }

    private class ImageLoaderTask extends AsyncTask<Pair<String, String>, Void, Boolean> {

        /**
         * Reads the bitmap from disk in the background and waits until it's loaded by pano widget.
         */
        @Override
        protected Boolean doInBackground(Pair<String, String>... fileInformation) {
            ;  // It's safe to use null VrPanoramaView.Options.
            InputStream istr = null;
            Log.d(TAG, "fileInformation NOT available");
            AssetManager assetManager = getAssets();
            try {
                istr = assetManager.open(fileInformation[0].first.toString() + "/" + fileInformation[0].second.toString());
            } catch (IOException e) {
                Log.e(TAG, "Could not decode default bitmap: " + e);
                return false;
            }

            VrPanoramaView.Options panoOptions = new VrPanoramaView.Options();
            panoOptions.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
            panoWidgetView.loadImageFromBitmap(BitmapFactory.decodeStream(istr), panoOptions);
            try {
                if (istr != null) {
                    istr.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Could not close input stream: " + e);
            }

            return true;
        }
    }

    public class ActivityEventListener extends VrPanoramaEventListener {

        @Override
        public void onLoadSuccess() {
            super.onLoadSuccess();
        }

        @Override
        public void onLoadError(String errorMessage) {
            super.onLoadError(errorMessage);
        }

        @Override
        public void onClick() {
            Log.d(TAG, "onClick");
            Intent intent = new Intent(PanoramaActivity.this, PanoramaActivity.class);
            String newFileName = null;
            if ("office".equalsIgnoreCase(folderName)) {
                switch (fileName) {
                    case "office_front.jpg":
                        newFileName = "office_reception.jpg";
                        break;
                    case "office_reception.jpg":
                        newFileName = "office_tech_floor.jpg";
                        break;
                    case "office_tech_floor.jpg":
                        newFileName = "canteen.jpg";
                        break;
                    case "canteen.jpg":
                        finish();
                        return;
                    default:
                        break;
                }
            } else if ("bda".equalsIgnoreCase(folderName)) {
                switch (fileName) {
                    case "bda_entrance.jpg":
                        newFileName = "bda_garden.jpg";
                        break;
                    case "bda_garden.jpg":
                        finish();
                        return;
                    default:
                        break;
                }
            } else if ("treebo_nova".equalsIgnoreCase(folderName)) {
                switch (fileName) {
                    case "nova_front.jpg":
                        newFileName = "nova_fd.jpg";
                        break;
                    case "nova_fd.jpg":
                        newFileName = "nova_room.jpg";
                        break;
                    case "nova_room.jpg":
                        newFileName = "nova_bathroom.jpg";
                        break;
                    case "nova_bathroom.jpg":
                        finish();
                        return;
                    default:
                        break;
                }
            } else {
                folderName = "other_places";
                newFileName = "andes.jpg";
            }

            intent.putExtra("fileName", newFileName);
            intent.putExtra("folderName", folderName);
            intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }
}
