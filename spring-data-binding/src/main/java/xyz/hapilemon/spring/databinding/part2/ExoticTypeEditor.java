package xyz.hapilemon.spring.databinding.part2;

import java.beans.PropertyEditorSupport;

public class ExoticTypeEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) {
        setValue(new ExoticType(text.toUpperCase()));
    }
}
