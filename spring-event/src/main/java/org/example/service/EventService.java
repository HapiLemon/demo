package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.event.ComplementStockInEvent;
import org.example.event.PalletizeEvent;
import org.example.po.EventPublishLog;
import org.example.publisher.CustomizePublisher;
import org.example.repository.EventPublishLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author keminfeng
 */
@Slf4j
@Service
public class EventService {

    @Autowired
    private EventPublishLogRepository eventPublishLogRepository;

    @Autowired
    private CustomizePublisher customizePublisher;

    @Transactional
    public void palletize() {
        log.info("Palletize start");

        log.info("Complement stock in start");
        ComplementStockInEvent complementStockInEvent = new ComplementStockInEvent("boxId" + Math.random(), "EP_GAR");
        createEventPublishLog("ComplementStockInEvent");
        customizePublisher.publish(complementStockInEvent);
        log.info("Complement stock in done");

        PalletizeEvent palletizeEvent = new PalletizeEvent("palletId" + Math.random(), List.of("boxId" + Math.random(), "boxId" + Math.random()), "EP_GAR");
        createEventPublishLog("PalletizeEvent");
        customizePublisher.publish(palletizeEvent);

        log.info("Palletize done");

    }

    public EventPublishLog createEventPublishLog(String event) {
        EventPublishLog eventPublishLog = new EventPublishLog();
        eventPublishLog.setEvent(event);
        eventPublishLog.setContent("{}");
        eventPublishLog.setEntity("BOX");
        String entityId = "TEST" + Math.random();
        eventPublishLog.setEntityId(entityId);
        return eventPublishLogRepository.save(eventPublishLog);
    }

}
