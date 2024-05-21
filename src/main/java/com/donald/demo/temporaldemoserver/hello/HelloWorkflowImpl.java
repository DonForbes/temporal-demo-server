package com.donald.demo.temporaldemoserver.hello;

import java.time.Duration;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.donald.demo.temporaldemoserver.hello.model.Person;

import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowInterface;


@WorkflowImpl(taskQueues = "HelloDemoTaskQueue")
public class HelloWorkflowImpl implements HelloWorkflow {

    public static final Logger logger = Workflow.getLogger(HelloWorkflowImpl.class);

    private HelloActivity activity =
    Workflow.newActivityStub(
        HelloActivity.class,
        ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(5)).build());

    @Override
    public String sayHello(Person person) {
        logger.info(person.toString());
        return activity.hello(person);
    } 

}
