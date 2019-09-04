package org.csuc.adapter;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @author amartinez
 */
public class BigIntegerAdapter extends JsonAdapter<BigInteger> {

    public BigIntegerAdapter() {
    }

    @Override
    public BigInteger fromJson(JsonReader jsonReader) throws IOException {
        String string = jsonReader.nextString();
        return new BigInteger(string);
    }

    @Override
    public void toJson(JsonWriter jsonWriter, BigInteger bigInteger) throws IOException {
        jsonWriter.value(bigInteger.toString());
    }

}
