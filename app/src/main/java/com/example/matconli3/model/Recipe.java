package com.example.matconli3.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;


import java.util.HashMap;
import java.util.Map;

@Entity
public class Recipe {

    @PrimaryKey
    //  @NonNull
    //  private String id;
    @NonNull
    private String name;
    private String imageUrl;
    private Long lastUpdated;


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        //    result.put("id", id);
        result.put("name", name);
        result.put("imageUrl", imageUrl);
        result.put("lastUpdated",FieldValue.serverTimestamp() );
        return result;
    }

    public void fromMap( Map<String, Object> map ){
        // id=(String)map.get("id");
        name=(String)map.get("name");
        imageUrl=(String)map.get("imageUrl");
        Timestamp ts=(Timestamp)map.get("lastUpdated");
        lastUpdated=ts.getSeconds();
        //long time=ts.toDate().getTime();
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }
    //    @NonNull
//    public String getId() {
//        return id;
//    }
    @NonNull
    public String getName() {
        return name;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    //    @NonNull
//    public void setId( String id) {
//        this.id = id;
//    }
    @NonNull
    public void setName(String name) {
        this.name = name;
    }
}
