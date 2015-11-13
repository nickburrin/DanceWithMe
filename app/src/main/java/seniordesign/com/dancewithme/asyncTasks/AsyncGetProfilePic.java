package seniordesign.com.dancewithme.asyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Created by nickburrin on 11/11/15.
 */
public class AsyncGetProfilePic extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = AsyncGetProfilePic.class.getSimpleName();

    private Exception exception;

    @Override
    protected Bitmap doInBackground(String... urls) {
        Bitmap bm = null;
        try {
            URL imageURL = new URL(urls[0]);
            bm = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bm;
    }

    protected void onPostExecute(Bitmap bm) {
        if(exception != null){
            exception.printStackTrace();
        } else {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ParseFile pf = new ParseFile(byteArray);
            ParseUser.getCurrentUser().put("ProfilePicture", pf);
            ParseUser.getCurrentUser().saveInBackground();

            Log.d(TAG, "Successfully returned bitmap for " + ParseUser.getCurrentUser().getUsername());
        }
    }
}
