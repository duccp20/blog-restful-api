package com.example.blogapprestapi.event;

import com.example.blogapprestapi.model.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private User user;
    private String url;

    public RegistrationCompleteEvent(User user, String url) {
        super(user);
        this.user = user;
        this.url = url;
    }
}
