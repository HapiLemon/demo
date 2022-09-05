package org.example.listener;

import lombok.extern.slf4j.Slf4j;
import org.example.event.ComplementStockInEvent;
import org.example.event.StockInEvent;
import org.example.publisher.CustomizePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author keminfeng
 */
@Slf4j
@Component
public class ComplementStockInEventListener {

    @Autowired
    private CustomizePublisher customizePublisher;

    @EventListener
//    @TransactionalEventListener(fallbackExecution = true, phase = TransactionPhase.BEFORE_COMMIT)
    public void complementStockInEventListener(ComplementStockInEvent complementStockInEvent) throws InterruptedException {

        log.info("Listener: complement box stock in");
        log.info("stock in warehouse: {}", complementStockInEvent.getWarehouse());
        log.info("boxId: {}", complementStockInEvent.getBoxId());

        Thread.sleep(2000L);

        StockInEvent stockInEvent = new StockInEvent(complementStockInEvent.getBoxId(), complementStockInEvent.getWarehouse());
        customizePublisher.publish(stockInEvent);

//        throw new RuntimeException("Complement Stock In Error");

    }

}
