package com.kafka.publisher;

import com.kafka.event.BlogEvent;
import com.kafka.event.UserEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/8/1 15:35
 */
@Service
public class UserEventPublisher implements ApplicationEventPublisherAware {

    private static ApplicationEventPublisher applicationEventPublisher;

    public void publish(Long eventId, String eventType, Long itemId, String itemName) {
        UserEvent userEvent = new UserEvent();
        userEvent.setEventId(eventId);
        userEvent.setEventType(String.format("userEventType:%s", eventType));
        userEvent.setUserId(itemId);
        userEvent.setUserName(String.format("userName:%s", itemName));
        applicationEventPublisher.publishEvent(userEvent);

        BlogEvent blogEvent = new BlogEvent();
        blogEvent.setEventId(eventId);
        blogEvent.setEventType(String.format("blogEvent:%s", eventType));
        blogEvent.setBlogId(itemId);
        blogEvent.setBlogName(String.format("blogName:%s", itemName));
        applicationEventPublisher.publishEvent(blogEvent);

        BlogEvent blogEvent2 = new BlogEvent();
        blogEvent2.setEventId(eventId);
        blogEvent2.setEventType(String.format("blogEvent2:%s", eventType));
        blogEvent2.setBlogId(itemId);
        blogEvent2.setBlogName(String.format("blogName2:%s", itemName));
        applicationEventPublisher.publishEvent(blogEvent2);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        applicationEventPublisher = publisher;
    }
}
