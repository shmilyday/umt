/*
 * Copyright (c) 2008-2013 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package cn.vlabs.umt.common.schedule;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;


public class ScheduleService {
	public void setUp(){
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		try {
			scheduler = schedulerFactory.getScheduler();
			scheduler.start();
			log.info("初始化定时器成功。");
		} catch (SchedulerException e) {
			log.error("初始化定时器失败:"+e.getMessage());
			log.debug("详细信息", e);
		}
	}
	public void tearDown(){
		log.info("正在关闭定时器。");
		if (scheduler!=null){
			try {
				scheduler.shutdown();
				scheduler=null;
			} catch (SchedulerException e) {
				log.error("关闭定时器失败。");
			}
			
		}
	}
	
	public void removeJob(String jobname, String triggername){
		try {
			scheduler.removeJobListener(jobname);
			scheduler.removeTriggerListener(triggername);
		} catch (SchedulerException e) {
			log.debug("无法移除已订阅的信息",e);
		}
	}
	public void schedule(int interval, String triggerName, String jobname, Class<?> jobclass,  String paramname, Object param){
		JobDetail jobDetail = new JobDetail(jobname, null, jobclass);
		if (param!=null)
		{
			jobDetail.getJobDataMap().put(paramname, param);
		}
		Trigger trigger= TriggerUtils.makeMinutelyTrigger(interval);
		trigger.setStartTime(TriggerUtils.getEvenMinuteDate(new Date()));
		trigger.setName(triggerName);
		try {
			scheduler.scheduleJob(jobDetail, trigger);
			log.info("成功添加新的定时任务:"+jobname);
		} catch (SchedulerException e) {
			log.error("添加新的定时任务"+jobname+"时失败:"+e.getMessage());
			log.info("详细信息");
		}
	}
	
	private Scheduler scheduler;
	private static final Logger log = Logger.getLogger(ScheduleService.class);
}