package com.gorigeek.springboot.schedule;

import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.gorigeek.springboot.util.LoggerUtils;

//import com.fimpe.gestor_documentos.model.SgConfiguration;

@Component
@Scope(value="singleton")
public class CronConfig {

	private static final Logger logger = LoggerFactory.getLogger(CronConfig.class);
	
	@Autowired
	TaskScheduler taskScheduler;
	
	@Autowired
	private NotificacionRunnable notificacionConfigurationService;
		
	private HashMap<String, String> cronMap = new HashMap<>();
	
	ScheduledFuture<?> vnotifySchedule = null;

	public CronConfig() {
		cronMap.put("cronotificacion", "0");
	}
	
	
	//cron para revisar los parametros
	@Scheduled(cron = "0 * * * * ?")
	public void run() {
		
		try{
			//Enviar notificaciones
			if("0".equals(cronMap.get("cronotificacion"))) {
			    vnotifySchedule = this.taskScheduler.schedule(notificacionConfigurationService, 
						new CronTrigger("0/59 * * * * ?"));
			}else {
				//esperar a que finalize el proceso si sigue ejecutandose
				vnotifySchedule.cancel(false);
				if (vnotifySchedule.isCancelled()) {
					//LoggerUtils.logSchedule(logger, "IF Inside Else ", vnotifySchedule.isCancelled() + "");
					vnotifySchedule = this.taskScheduler.schedule(notificacionConfigurationService, 
							new CronTrigger("0/59 * * * * ?"));
				}
			}
			cronMap.put("cronotificacion", "0/59 * * * * ?");
		} catch(NullPointerException e){
			logger.error("Parametos de cron nulos en base de datos ", e);
		} catch(Exception e){
			logger.error("Error de ejecución ", e);
		}
		//LoggerUtils.logSchedule(logger, "CRON_CONFIG", "Termina Task to check schedules configuration");
	}
	
}
