package sample.s2jdbc.entity;

import org.seasar.extension.jdbc.name.PropertyName;

public class UserName {

    public static PropertyName<String> name() {
        return new PropertyName<>("name");
    }
}
