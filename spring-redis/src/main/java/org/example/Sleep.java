package org.example;

/**
 * @author keminfeng
 */
public interface Sleep {

    default void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignore) {
        }
    }

}
