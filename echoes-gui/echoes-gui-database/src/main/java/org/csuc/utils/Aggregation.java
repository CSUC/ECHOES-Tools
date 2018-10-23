package org.csuc.utils;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

/**
 * @author amartinez
 */
public class Aggregation {

    private String _id;
    private Long total;

    public Aggregation() {}

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        JsonAdapter<Aggregation> jsonAdapter = new Moshi.Builder().build().adapter(Aggregation.class);
        return jsonAdapter.toJson(this);
    }
}
