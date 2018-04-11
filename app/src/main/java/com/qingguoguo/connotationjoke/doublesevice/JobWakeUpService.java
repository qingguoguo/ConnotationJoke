package com.qingguoguo.connotationjoke.doublesevice;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.List;

/**
 * 作者:qingguoguo
 * 创建日期：2018/4/12 on 0:23
 * 描述:
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobWakeUpService extends JobService {
    private final static int JOB_WAKEUP_SERVICE_ID = 2;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //开启轮寻
        ComponentName componentName = new ComponentName(this, JobWakeUpService.class);
        JobInfo.Builder jobBuilder = new JobInfo.Builder(JOB_WAKEUP_SERVICE_ID, componentName);
        //设置轮寻时间
        jobBuilder.setPeriodic(5000);
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobBuilder.build());
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        //开启定时任务，轮寻，看MessageService有没有被杀死
        if (!serviceAlive(MessageService.class.getName())) {
            startService(new Intent(this, MessageService.class));
        }
        return false;
    }


    /**
     * 判断服务是否还活着
     *
     * @param serviceName 包名+服务的类名
     */
    private boolean serviceAlive(String serviceName) {
        boolean isWork = false;
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
        if (runningServices.isEmpty()) {
            return false;
        }
        for (int i = 0; i < runningServices.size(); i++) {
            String className = runningServices.get(i).service.getClassName();
            if (serviceName.equals(className)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
