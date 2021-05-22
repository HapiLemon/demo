package xyz.hapilemon.spring.databinding.part4;

import java.beans.PropertyEditorSupport;

public class ExoticTypeBinder extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) {
        setValue(new ExoticType(text.toUpperCase()));
    }
}
