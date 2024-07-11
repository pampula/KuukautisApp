package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.dbservice;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.TimePeriod;

/**
 * Serializes TimePeriod object to Json and deserializes it to the correct type.
 * This class was written with the help of ChatGPT.
 */
public class TimePeriodTypeAdapter implements JsonSerializer<TimePeriod>, JsonDeserializer<TimePeriod> {
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final String JSON_FIELD_LENGTH = "length";
    private static final String JSON_FIELD_FIRST_DAY = "firstDay";
    private static final String JSON_FIELD_LAST_DAY = "lastDay";

    /**
     * Serializes TimePeriod object to Json.
     * @param src The object that needs to be converted to Json
     * @param typeOfSrc The actual type (fully genericized version) of the source object
     * @param context Json serialization context
     * @return JsonElement Produced Json element
     */
    @Override
    public JsonElement serialize(TimePeriod src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JSON_FIELD_LENGTH, src.getLength());
        jsonObject.addProperty(JSON_FIELD_FIRST_DAY, formatDate(src.getFirstDay()));
        jsonObject.addProperty(JSON_FIELD_LAST_DAY, formatDate(src.getLastDay()));
        return jsonObject;
    }

    /**
     * Deserializes Json element to TimePeriod object.
     * @param json The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context Json serialization context
     * @return TimePeriod Produced TimePeriod object
     * @throws JsonParseException
     */
    @Override
    public TimePeriod deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        int length = jsonObject.get(JSON_FIELD_LENGTH).getAsInt();
        Calendar firstDay = parseDate(jsonObject.get(JSON_FIELD_FIRST_DAY).getAsString());
        Calendar lastDay = parseDate(jsonObject.get(JSON_FIELD_LAST_DAY).getAsString());

        return new TimePeriod(length, firstDay, lastDay);
    }

    /**
     * Formats Calendar object to String.
     * @param calendar Calendar object to format
     * @return String Date
     */
    private String formatDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(calendar.getTime());
    }

    /**
     * Creates new Calendar object with wanted date.
     * @param dateStr Date to set to the Calendar
     * @return Calendar Produced Calendar object
     */
    private Calendar parseDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = dateFormat.parse(dateStr);
            calendar.setTime(date);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }
}

