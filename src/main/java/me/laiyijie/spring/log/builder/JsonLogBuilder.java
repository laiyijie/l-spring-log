package me.laiyijie.spring.log.builder;

import me.laiyijie.spring.log.WebLogUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by admin on 2017-05-19.
 */
public abstract class JsonLogBuilder {
    protected JSONObject data;
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZ");

    protected JsonLogBuilder() {
        data = new JSONObject();
        put("_TIME_", dateFormat.format(new Date(System.currentTimeMillis())));
        put("_LOGGER_", getLogger().getName());
    }

    public JsonLogBuilder put(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public JsonLogBuilder put(HttpServletResponse response) {
        return this.put("P_HEADERS", WebLogUtils.getAllHeaders(response))
                .put("P_CODE", response.getStatus());
    }

    public JsonLogBuilder put(HttpServletRequest request, String userSessionKey) {
        return this.put("Q_USERNAME", WebLogUtils.getSessionUsername(request, userSessionKey))
                .put("Q_METHOD", request.getMethod())
                .put("Q_URL", WebLogUtils.getRequestUrl(request))
                .put("Q_PARAM", WebLogUtils.getRequestParams(request))
                .put("Q_HEADERS", WebLogUtils.getAllHeaders(request));
    }

    public JsonLogBuilder put(Throwable throwable) {
        return this.put("E_TRACE", WebLogUtils.getTrace(throwable));
    }

    public JsonLogBuilder putAll(Map<? extends String, ?> ob) {
        data.putAll(ob);
        return this;
    }

    public JSONObject getData() {
        return data;
    }

    public JsonLogBuilder clear() {
        data = new JSONObject();
        return this;
    }

    public void log() {
        getLogger().info(getData().toJSONString());
    }

    protected abstract Logger getLogger();
}
