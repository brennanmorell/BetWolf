import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzMain {
	public static void main(String[] args){
		//Define a job and tie it to our job class
		JobDetail job = JobBuilder.newJob(QuartzJob.class).build();
		//Trigger t = TriggerBuilder.newTrigger().withIdentity("Simple Trigger").startNow().build();
		Trigger t = TriggerBuilder.newTrigger().withIdentity("MinTrigger").withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * 1/1 * ? *")).build();
		//Trigger t = TriggerBuilder.newTrigger().withIdentity("CronTrigger").withSchedule(CronScheduleBuilder.cronSchedule("0 0/30 * 1/1 * ? *")).build();
		Scheduler s;
		try {
			s = StdSchedulerFactory.getDefaultScheduler();
			try {
				s.start();
				s.scheduleJob(job, t);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		} catch (SchedulerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
