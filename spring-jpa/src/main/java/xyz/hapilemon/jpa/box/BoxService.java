package xyz.hapilemon.jpa.box;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author keminfeng
 */
@Service
public class BoxService {

    @Autowired
    private BoxRepository boxRepository;

    public Box findById(String id) {
        return this.boxRepository.findById(id).orElse(null);
    }
}
