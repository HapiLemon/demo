package xyz.hapilemon.jpa.pallet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import xyz.hapilemon.jpa.box.Box;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author keminfeng
 */
@Data
@Entity
public class Pallet {

    @Id
    private String id;

    private String warehouse;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "palletId", referencedColumnName = "id")
    private List<Box> boxList;
}
