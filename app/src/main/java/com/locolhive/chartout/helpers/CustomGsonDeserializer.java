package com.locolhive.chartout.helpers;

import android.util.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.locolhive.chartout.classes.StringArrayObject;
import java.lang.reflect.Type;

public class CustomGsonDeserializer implements JsonDeserializer<StringArrayObject> {

  private static final String TAG = CustomGsonDeserializer.class.getSimpleName() + " YOYO";

  @Override
  public StringArrayObject deserialize(JsonElement jElement, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {

    JsonObject jObject = jElement.getAsJsonObject();
    for (String s : jObject.keySet()) {
      JsonElement jsonElement = jObject.get(s);
      if (jsonElement.isJsonArray()) {
        JsonArray asJsonArray = jsonElement.getAsJsonArray();
        for (JsonElement element : asJsonArray) {
          Log.i(TAG, "deserialize: " + element.getAsString());
        }
      }
    }
    return new StringArrayObject();
  }
}