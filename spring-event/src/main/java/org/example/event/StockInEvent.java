package org.example.event;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author keminfeng
 */
@Data
@AllArgsConstructor
public class StockInEvent {

    private String boxId;

    private String warehouse;

}
