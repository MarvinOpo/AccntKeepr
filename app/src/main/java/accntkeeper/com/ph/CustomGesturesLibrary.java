package accntkeeper.com.ph;

import java.io.File;

import android.content.Context;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;

public class CustomGesturesLibrary {

    private static GestureLibrary sStore;

    public static GestureLibrary getStore(Context c) {

        if (sStore == null) {
            File storeFile = new File(c.getFilesDir(), "gesturesHackathon");
            sStore = GestureLibraries.fromFile(storeFile);
            sStore.load();
        }

        return sStore;
    }

}
