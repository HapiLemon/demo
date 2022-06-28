package xyz.hapilemon.jpa.pallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author keminfeng
 */
@Repository
public interface PalletRepository extends JpaRepository<Pallet, String>, JpaSpecificationExecutor<Pallet> {

}
