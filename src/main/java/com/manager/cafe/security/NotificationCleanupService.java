package com.manager.cafe.security;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manager.cafe.repository.NotificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import org.slf4j.LoggerFactory;

@Service
public class NotificationCleanupService {

    @Autowired
    private NotificationRepository notificationRepo;
    private static final Logger log =
            LoggerFactory.getLogger(NotificationCleanupService.class);

    // RUN EVERY DAY AT 2 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void deleteOldNotifications() {

        LocalDateTime cutoff =
                LocalDateTime.now().minusDays(3);

        notificationRepo.deleteByCreatedAtBefore(cutoff);

        log.info("Old notifications deleted successfully");
    }
}
