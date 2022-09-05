package org.example.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

/**
 * @author keminfeng
 */
@Data
@AllArgsConstructor
public class PalletizeEvent {

    private String palletId;

    private Collection<String> boxIds;

    private String warehouse;

}

