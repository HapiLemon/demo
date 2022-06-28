package xyz.hapilemon.jpa.pallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author keminfeng
 */
@Service
public class PalletService {

    @Autowired
    private PalletRepository palletRepository;

    public Pallet findById(String id) {
        return this.palletRepository.findById(id).orElse(null);
    }
}
