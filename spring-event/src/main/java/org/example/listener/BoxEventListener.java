package org.example.listener;

import lombok.extern.slf4j.Slf4j;
import org.example.event.StockInEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author keminfeng
 */

@Slf4j
@Component
public class BoxEventListener {

    //    @Async
    @TransactionalEventListener(fallbackExecution = true, phase = TransactionPhase.BEFORE_COMMIT)
    public void stockInEventListener(StockInEvent stockInEvent) {

        log.info("Listener: box stock in");
        log.info("stock in warehouse: {}", stockInEvent.getWarehouse());
        log.info("boxId: {}", stockInEvent.getBoxId());

//        throw new RuntimeException("Stock In Listener Error");

    }

}
