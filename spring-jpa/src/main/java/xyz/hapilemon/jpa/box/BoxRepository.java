package xyz.hapilemon.jpa.box;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author keminfeng
 */
@Repository
public interface BoxRepository extends JpaRepository<Box, String>, JpaSpecificationExecutor<Box> {

}
