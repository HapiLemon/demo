package xyz.hapilemon.jpa.box;

import lombok.Data;
import xyz.hapilemon.jpa.pallet.Pallet;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author keminfeng
 */
@Data
@Entity
public class Box {

    @Id
    private String id;

    private String palletId;

    @ManyToOne
    @JoinColumn(name = "palletId", insertable = false, updatable = false)
    private Pallet pallet;

}
