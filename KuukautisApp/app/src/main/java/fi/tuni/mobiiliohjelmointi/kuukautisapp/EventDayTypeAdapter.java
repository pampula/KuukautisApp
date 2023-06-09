package fi.tuni.mobiiliohjelmointi.kuukautisapp;

import android.annotation.SuppressLint;

import com.applandeo.materialcalendarview.EventDay;
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

/**
 * Serializes EventDay object to Json and deserializes it to the correct type.
 * This class was written with the help of ChatGPT.
 */
public class EventDayTypeAdapter implements JsonSerializer<EventDay>, JsonDeserializer<EventDay> {
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final String FLOW_TYPE = "flow";
    private static final String PREDICTION_TYPE = "prediction";
    private static final String JSON_FIELD_DATE = "date";
    private static final String JSON_FIELD_DRAWABLE_TYPE = "drawableType";

    /**
     * Serializes EventDay object to Json.
     * @param src the object that needs to be converted to Json
     * @param typeOfSrc the actual type (fully generalized version) of the source object
     * @param context Json serialization context
     * @return JsonElement Produced Json element
     */
    @Override
    public JsonElement serialize(EventDay src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JSON_FIELD_DATE, formatDate(src.getCalendar()));
        @SuppressLint("RestrictedApi") Object drawableType = src.getImageDrawable();
        if (drawableType.equals(R.drawable.circle_flow)) {
            jsonObject.addProperty(JSON_FIELD_DRAWABLE_TYPE, FLOW_TYPE);
        }
        else {
            jsonObject.addProperty(JSON_FIELD_DRAWABLE_TYPE, PREDICTION_TYPE);
        }

        return jsonObject;
    }

    /**
     * Deserializes Json element to EventDay object.
     * @param json The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context Json serialization context
     * @return EventDay Produced EventDay object
     * @throws JsonParseException
     */
    @Override
    public EventDay deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Calendar date = parseDate(jsonObject.get(JSON_FIELD_DATE).getAsString());
        String drawableType = jsonObject.get(JSON_FIELD_DRAWABLE_TYPE).getAsString();
        EventDay eventDay;

        if(drawableType.equals(FLOW_TYPE)) {
            eventDay = new EventDay(date, R.drawable.circle_flow);
        }
        else {
            eventDay = new EventDay(date, R.drawable.circle_prediction);
        }

        return eventDay;
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

