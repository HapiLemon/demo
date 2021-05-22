package xyz.hapilemon.spring.databinding.part3;

import java.beans.PropertyEditorSupport;

public class ExoticTypeBinder extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) {
        setValue(new ExoticType(text.toLowerCase()));
    }
}
