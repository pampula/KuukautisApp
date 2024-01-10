package fi.tuni.mobiiliohjelmointi.kuukautisapp;

import android.content.Context;

import com.applandeo.materialcalendarview.EventDay;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Saves and retrieves data from the phone's internal storage.
 * This class was written with the help of ChatGPT.
 */
public class InternalStorageManager {

    private static final String FILE_NAME = "user_data.json";
    private static final int MAX_MARKINGS_AMOUNT = 365;
    private static final int MAX_CYCLE_LENGTHS_AMOUNT = 12;
    private static final int MAX_FLOW_LENGTHS_AMOUNT = 12;

    /**
     * Checks if there is already user data saved.
     * @param context Saving context
     * @return True if there is user data available
     */
    public static boolean userExists(Context context) {
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            int fileSize = fis.available();
            fis.close();
            return fileSize > 0;
        }
        catch (IOException e) {
            return false;
        }
    }

    /**
     * Deletes all user data from internal storage.
     * @param context Data saving context
     */
    public static void clearData(Context context) {
        context.deleteFile(FILE_NAME);
    }

    /**
     * Saves user data to internal storage.
     * @param context Saving context
     * @param user User object to be saved
     */
    public static void saveUser(Context context, User user) {
        Gson gson = createGson();
        User updatedUser = limitCalendarMarkings(user);
        updatedUser = limitCycleLengthValues(updatedUser);
        updatedUser = limitFlowLengthValues(updatedUser);
        String json = gson.toJson(updatedUser);

        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(json);
            osw.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Makes sure that the amount of calendar markings is limited to save memory.
     * @param user User that has the calendar data
     * @return User Updated user
     */
    private static User limitCalendarMarkings(User user) {
        List<EventDay> markings = user.getCalendarMarkings();
        int markingsAmount = markings.size();

        if (markingsAmount > MAX_MARKINGS_AMOUNT) {
            int elementsToDelete = markingsAmount - MAX_MARKINGS_AMOUNT;
            markings.subList(0, elementsToDelete).clear();
            user.setCalendarMarkings(markings);
        }
        return user;
    }

    /**
     * Makes sure that the amount of cycle length values is limited to save memory.
     * @param user User that has the cycle length value data
     * @return User Updated user
     */
    private static User limitCycleLengthValues(User user) {
        List<TimePeriod> values = user.getCycleLengths();
        int valuesAmount = values.size();

        if (valuesAmount > MAX_CYCLE_LENGTHS_AMOUNT) {
            int elementsToDelete = valuesAmount - MAX_CYCLE_LENGTHS_AMOUNT;
            values.subList(0, elementsToDelete).clear();
            user.setCycleLengths(values);
        }
        return user;
    }

    /**
     * Makes sure that the amount of flow length values is limited to save memory.
     * @param user User that has the flow length value data
     * @return User Updated user
     */
    private static User limitFlowLengthValues(User user) {
        List<TimePeriod> values = user.getFlowLengths();
        int valuesAmount = values.size();

        if (valuesAmount > MAX_FLOW_LENGTHS_AMOUNT) {
            int elementsToDelete = valuesAmount - MAX_FLOW_LENGTHS_AMOUNT;
            values.subList(0, elementsToDelete).clear();
            user.setFlowLengths(values);
        }
        return user;
    }

    /**
     * Fetches user data from internal storage and builds User object.
     * @param context Saving context
     * @return User Fetched user data
     */
    public static User loadUser(Context context) {
        Gson gson = createGson();
        User user = null;

        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonBuilder.append(line);
            }
            br.close();

            String json = jsonBuilder.toString();
            user = gson.fromJson(json, User.class);
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * Generates Gson builder that can handle custom EventDay and TimePeriod types.
     * @return Gson Gson builder
     */
    private static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(EventDay.class, new EventDayTypeAdapter());
        gsonBuilder.registerTypeAdapter(TimePeriod.class, new TimePeriodTypeAdapter());
        return gsonBuilder.create();
    }

}
