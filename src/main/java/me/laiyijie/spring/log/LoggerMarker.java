package me.laiyijie.spring.log;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public interface LoggerMarker {

	Marker BUSINESS_LOG = MarkerManager.getMarker("business");
	Marker ACCESS_LOG = MarkerManager.getMarker("access");
	Marker MONITOR_LOG = MarkerManager.getMarker("monitor");
}
