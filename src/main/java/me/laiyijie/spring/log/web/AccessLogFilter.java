package me.laiyijie.spring.log.web;

import me.laiyijie.spring.log.LLog;
import org.apache.commons.io.output.TeeOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mock.web.DelegatingServletOutputStream;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

public class AccessLogFilter extends OncePerRequestFilter {
    private static final Logger logger = LogManager.getLogger(AccessLogFilter.class);

    private String usernameKey = "username";
    private Integer payloadMaxLength = 1024;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(baos);
        Long startTime = System.currentTimeMillis();
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        String requestPayload = getPayLoad(requestWrapper.getContentAsByteArray(),
                response.getCharacterEncoding());
        request.getSession().getAttribute(usernameKey);
        filterChain.doFilter(requestWrapper, new HttpServletResponseWrapper
                (response) {
            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return new DelegatingServletOutputStream(
                        new TeeOutputStream(super.getOutputStream(), ps)
                );
            }

            @Override
            public PrintWriter getWriter() throws IOException {
                return new PrintWriter(new DelegatingServletOutputStream(
                        new TeeOutputStream(super.getOutputStream(), ps))
                );
            }
        });

        String responsePayload = getPayLoad(baos.toByteArray(),
                response.getCharacterEncoding());
        LLog.accessJsonLogBuilder()
                .addRequestPayLoad(requestPayload)
                .addResponsePayLoad(responsePayload)
                .put(request, usernameKey)
                .put(response)
                .put("_COST_", System.currentTimeMillis() - startTime)
                .log();
    }

    private String getPayLoad(byte[] buf, String characterEncoding) {
        String payload = "";
        if (buf == null) return payload;
        if (buf.length > 0) {
            int length = Math.min(buf.length, getPayloadMaxLength());
            try {
                payload = new String(buf, 0, length, characterEncoding);
            } catch (UnsupportedEncodingException ex) {
                payload = "[unknown]";
            }
        }
        return payload;
    }


    public String getUsernameKey() {
        return usernameKey;
    }

    public void setUsernameKey(String usernameKey) {
        this.usernameKey = usernameKey;
    }

    public Integer getPayloadMaxLength() {
        return payloadMaxLength;
    }

    public void setPayloadMaxLength(Integer payloadMaxLength) {
        this.payloadMaxLength = payloadMaxLength;
    }
}
