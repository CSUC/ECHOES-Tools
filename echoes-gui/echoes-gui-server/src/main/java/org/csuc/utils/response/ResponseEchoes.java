package org.csuc.utils.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.List;

/**
 * @author amartinez
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ResponseEchoes {

    private String _id;
    private int _size;
    private int _total_pages;
    private int _returned;

    private List _embedded;

    public ResponseEchoes() {}

    public ResponseEchoes(String id, int size, int total_pages, int returned, List _embedded) {
        this._id = id;
        this._size = size;
        this._total_pages = total_pages;
        this._returned = returned;
        this._embedded = _embedded;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int get_size() {
        return _size;
    }

    public void set_size(int _size) {
        this._size = _size;
    }

    public int get_total_pages() {
        return _total_pages;
    }

    public void set_total_pages(int _total_pages) {
        this._total_pages = _total_pages;
    }

    public int get_returned() {
        return _returned;
    }

    public void set_returned(int _returned) {
        this._returned = _returned;
    }

    public List get_embedded() {
        return _embedded;
    }

    public void set_embedded(List _embedded) {
        this._embedded = _embedded;
    }

    @Override
    public String toString() {
        JsonAdapter<ResponseEchoes> jsonAdapter = new Moshi.Builder().build().adapter(ResponseEchoes.class);

        //JsonAdapter<ResponseEchoes<T>> jsonAdapter = new Moshi.Builder().build().adapter(ResponseEchoes.class);
        return jsonAdapter.toJson(this);
    }
}
