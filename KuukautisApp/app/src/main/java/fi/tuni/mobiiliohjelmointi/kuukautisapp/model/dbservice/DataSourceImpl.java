package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.dbservice;

import com.applandeo.materialcalendarview.EventDay;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.TimePeriod;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.UserData;

/**
 * Saves and retrieves data from the phone's internal storage.
 */
public class DataSourceImpl implements DataSource {

    private final String FILE_NAME = "user_data.json";
    //TODO: switch into Firebase or MongoDB (saving format a list of dates and ints for analysed values)

    public boolean userExists(String userId) {
        /*
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            int fileSize = fis.available();
            fis.close();
            return fileSize > 0;
        }
        catch (IOException e) {
            return false;
        }
        */
        return false;
    }

    public boolean truncateUser(String userId) {
        /*
        // Lol let's just delete the whole thing :DDD
        context.deleteFile(FILE_NAME);
        return true;
         */
        return false;
    }
    public boolean deleteUser(String userId) {
        /*
        context.deleteFile(FILE_NAME);
        return true;

         */
        return false;
    }

    public boolean saveUserData(UserData user) {
        /*
        Gson gson = createGson();
        String json = gson.toJson(user);

        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(json);
            osw.close();
        }

        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;

         */
        return false;
    }


    public UserData getUserData(String userId) {
        /*
        Gson gson = createGson();
        UserData user = null;

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
            user = gson.fromJson(json, UserData.class);
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return user;
         */
        return null;
    }

    /**
     * Generates Gson builder that can handle custom EventDay and TimePeriod types.
     * @return Gson Gson builder
     */
    private Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(EventDay.class, new EventDayTypeAdapter());
        gsonBuilder.registerTypeAdapter(TimePeriod.class, new TimePeriodTypeAdapter());
        return gsonBuilder.create();
    }

}