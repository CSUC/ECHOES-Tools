package org.csuc.adapter;

import com.squareup.moshi.*;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author amartinez
 */
public class LocalDateTimeAdapter extends JsonAdapter<LocalDateTime> {

    public LocalDateTimeAdapter() {
    }

    @Override
    public LocalDateTime fromJson(JsonReader jsonReader) throws IOException {
        String string = jsonReader.nextString();
        return LocalDateTime.parse(string);
    }

    @Override
    public void toJson(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        jsonWriter.value(localDateTime.toString());
    }

}
