/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
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
/**
 * 
 */
package cn.vlabs.umt.common.job;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

/**
 * @author lvly
 * @since 2013-5-29
 */
public class JobThread extends Thread{
	private static final Logger LOG=Logger.getLogger(JobThread.class);
	private static LinkedBlockingQueue<Jobable> jobQueue = new LinkedBlockingQueue<Jobable>();
	private static final long SLEEP_TIME=1000;
	private boolean flag=true;
	@Override
	public void run() {
		loop:while(flag){
			Jobable job;
			try {
				if(jobQueue.isEmpty()){
					LOG.info("job thread is empty, Sleep.....");
					Thread.sleep(SLEEP_TIME);
				}
				job = jobQueue.take();
				if (job == null) {
					continue;
				}
				for(Iterator<Jobable> it=jobQueue.iterator();it.hasNext();){
					Jobable tmpJob=it.next();
					if(tmpJob.isJobEquals(job)){
						LOG.warn("job equals ,stop this job["+job.getJobId()+"]");
						continue loop;
					}
				}
				long start=System.currentTimeMillis();
				LOG.info(job.getJobId()+":start!");
				job.doJob();
				LOG.info(job.getJobId()+":end!,cost="+(System.currentTimeMillis()-start)/1000+",last:"+jobQueue.size()+"job");
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				continue;
			}
		}
	}
	/**
	 * 把新增的任务对象，往对象里面放
	 * @param job
	 */
	public static synchronized void addJobThread(Jobable job){
		LOG.info("put job ["+job.getJobId()+"] into list!");
		jobQueue.add(job);
	}
	@Override
	public void interrupt() {
		LOG.info("job thread stoping...");
		this.flag=false;
		super.interrupt();
		LOG.info("job thread stoped");
	}

}
