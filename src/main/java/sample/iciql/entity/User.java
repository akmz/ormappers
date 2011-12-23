package sample.iciql.entity;

import com.iciql.Iciql.IQColumn;

public class User {

    @IQColumn(primaryKey = true, autoIncrement = true)
    public Long id;

    public String name;

    public Long version;
}
