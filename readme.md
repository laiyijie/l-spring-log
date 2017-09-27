# l-spring-log

## 使用方法

1. 设置环境变量 L_LOGPATH，此变量为日志位置
    L_LOGPATH=F:\logs
2. 增加filter
    @Bean
    public Filter loggingFilter() {
        return new AccessLogFilter();
    }
    