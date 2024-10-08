package com.gorigeek.springboot.util;

import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoggerUtils {
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static void logRequest(Logger logger, String request, String user) {
		logger.info("Req:["+ request +"] by " + user);
	}
	
	public static void logRequest(Logger logger, String request, String user, Long id) {
		logger.info("Req:["+ request +" - with id:"+ id +"] by " + user);
	}
	
	public static void logResponse(Logger logger, String response) {
		logger.info("Res:["+ response +"]");
	}
	
	public static void logRequest(Logger logger, String request, String user, String key) {
		logger.info("Req:["+ request +" - with key:"+ key +"] by " + user);
	}
	
	public static void logResponse(Logger logger, String response, Object value) {
		try {
			logger.info("Res:["+ response +"]=>[" + mapper.writeValueAsString(value) + "]");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	/*public static void logException(Logger logger, ApiError apierror) {
		logger.error("Exception: [" + apierror.toString() +"]");
	}*/
	
	public static void logProcessRequest(Logger logger, String request, String user, String referencia) {
		logger.info("Req:["+ request +" - with reference: "+ referencia +"] by " + user);
	}
	
	public static void logProcessService(Logger logger, String entity, String accion, String referencia) {
		logger.info(entity + ":[" + accion + " - with reference: " + referencia + "]");
	}
	
	public static void logProcessService(Logger logger, String entity, String accion, int id) {
		logger.info(entity + ":[" + accion + " - with id: " + id + "]");
	}
	
	public static void logSchedule(Logger logger, String scheduleName, String mensaje) {
		logger.info("Schedule:[" + scheduleName + ": " + mensaje + "]");
	}
	
	public static void logSchedule(Logger logger, String scheduleName, String mensaje, String referencia) {
		logger.info("Schedule:[" + scheduleName + ": " + mensaje + " - with reference: " + referencia + "]");
	}
	
	public static void logMail(Logger logger, String processName, String mensaje) {
		logger.info("Mail:[" + processName + ": " + mensaje + "]");
	}
	
	public static void logMail(Logger logger, String processName, String mensaje, String referencia) {
		logger.info("Mail:[" + processName + ": " + mensaje + " - with reference: " + referencia + "]");
	}
	
	public static void logMail(Logger logger, String processName, String mensaje, int id) {
		logger.info("Mail:[" + processName + ": " + mensaje + " - with id: " + id + "]");
	}
}
