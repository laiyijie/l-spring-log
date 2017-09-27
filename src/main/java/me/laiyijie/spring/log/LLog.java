package me.laiyijie.spring.log;

import me.laiyijie.spring.log.builder.AccessJsonLogBuilder;
import me.laiyijie.spring.log.builder.BusinessJsonLogBuilder;
import me.laiyijie.spring.log.builder.ErrorJsonLogBuilder;

/**
 * Created by admin on 2017-05-19.
 */
public class LLog {

    public static BusinessJsonLogBuilder businessJsonLogBuilder(String module) {
        return new BusinessJsonLogBuilder(module);
    }

    public static AccessJsonLogBuilder accessJsonLogBuilder() {
        return new AccessJsonLogBuilder();
    }

    public static ErrorJsonLogBuilder errorJsonLogBuilder() {
        return new ErrorJsonLogBuilder();
    }

}
