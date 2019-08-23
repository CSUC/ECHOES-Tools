package org.csuc.utils.dashboard;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

/**
 * @author amartinez
 */
public class AggregationYear {

    private Identifier _id;
    private Long total;

    public AggregationYear() {
    }

    public Identifier get_id() {
        return _id;
    }

    public void set_id(Identifier _id) {
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
        JsonAdapter<AggregationYear> jsonAdapter = new Moshi.Builder().build().adapter(AggregationYear.class);
        return jsonAdapter.toJson(this);
    }
}
