package org.example.repository;

import org.example.po.EventPublishLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author keminfeng
 */
public interface EventPublishLogRepository extends JpaRepository<EventPublishLog, String> {
   

}
