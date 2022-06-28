package xyz.hapilemon.jpa.box;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author keminfeng
 */
@RestController
@RequestMapping("box")
public class BoxController {

    @Autowired
    private BoxService boxService;

    @GetMapping("/{id}")
    public Box findById(@PathVariable String id) {
        return this.boxService.findById(id);
    }

}
