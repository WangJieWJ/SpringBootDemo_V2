package com.kafka.listener;

import com.kafka.event.BlogEvent;
import com.kafka.event.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/8/1 15:33
 */
@Service
public class ApplicationEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationEventListener.class);

    @EventListener
    public void userEvent(UserEvent userEvent) {
        LOGGER.info("【print by userEvent】{}", userEvent);
    }

    /**
     * 如果返回值不是void的，将会再次触发事件传递，接收BlogEvent事件,重新触发UserEvent事件
     * @param blogEvent
     * @return
     */
    @EventListener
    public UserEvent blogEvent1(BlogEvent blogEvent) {
        LOGGER.info("【print by blogEvent1】{}", blogEvent);
        UserEvent userEvent = new UserEvent();
        userEvent.setUserId(blogEvent.getBlogId());
        userEvent.setUserName(blogEvent.getBlogName());
        return userEvent;
    }

    @EventListener
    public void blogEvent2(BlogEvent blogEvent) {
        LOGGER.info("【print by blogEvent2】{}", blogEvent);
    }
}
