package com.minibooking.resource.conf;

import com.minibooking.resource.adapters.out.ResourceEventPublisher;
import com.minibooking.resource.application.OnBookingRequested;
import org.springframework.context.annotation.*;

@Configuration
public class Beans {
    @Bean OnBookingRequested onBookingRequested(ResourceEventPublisher pub){ return new OnBookingRequested(pub); }
}
