package xyz.hapilemon.jpa.pallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.hapilemon.jpa.box.Box;

import java.util.List;

/**
 * @author keminfeng
 */
@RestController
@RequestMapping("pallet")
public class PalletController {

    @Autowired
    private PalletService palletService;

    @GetMapping("/{id}")
    public Pallet findById(@PathVariable String id) {
        Pallet pallet = this.palletService.findById(id);
        List<Box> boxList = pallet.getBoxList();
        pallet.setBoxList(boxList);
        return pallet;
    }

}
