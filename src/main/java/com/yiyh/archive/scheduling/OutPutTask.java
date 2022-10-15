package com.yiyh.archive.scheduling;

import com.yiyh.archive.service.FF7ReArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Lazy(false)
@Component
@EnableScheduling
public class OutPutTask implements SchedulingConfigurer {

    @Autowired
    private FF7ReArchiveService ff7ReArchiveService;
    // 默认的cron表达式
    @Value("${cron}")
    private String cron;

    // 定时任务的执行顺序：由上而下按顺序执行，当服务启动时或者定时任务时间到了，先执行业务逻辑，再设置定时任务
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(() -> {
            ff7ReArchiveService.zipFile();
            ff7ReArchiveService.cleanExpiredFile();
        }, triggerContext -> {
            // 此代码块用于动态拿到cron表达式并设置定时任务，当定时任务时间到了，就会重新获取cron表达式，重新设置定时任务
            CronTrigger trigger;
            try {
                // 一旦设置，立即生效
                trigger = new CronTrigger(cron);
                return trigger.nextExecutionTime(triggerContext);
            } catch (Exception e) {
                // 如果格式有问题就按默认时间（时间为每天23点55分）
                e.printStackTrace();
                trigger = new CronTrigger("0 55 23 ? * *");
                return trigger.nextExecutionTime(triggerContext);
            }
        });
    }

}
