package org.example.listener;

import lombok.extern.slf4j.Slf4j;
import org.example.event.PalletizeEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author keminfeng
 */
@Slf4j
@Component
public class PalletizeEventListener {

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    public void palletizeListener(PalletizeEvent palletizeEvent) {

        log.info("Listener: Palletize boxes to pallet");
        log.info("palletize warehouse: {}", palletizeEvent.getWarehouse());
        log.info("palletId: {}", palletizeEvent.getPalletId());
        log.info("boxId: {}", String.join(",", palletizeEvent.getBoxIds()));

//        throw new RuntimeException("Palletize Event Listener Error");
    }

}
