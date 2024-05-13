package com.donald.demo.temporaldemoserver;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.donald.demo.temporaldemoserver.hello.HelloWorkflow;
import com.donald.demo.temporaldemoserver.hello.model.Person;


import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
public class TemporalServerDemoRESTController {
  private static final Logger logger = LoggerFactory.getLogger(TemporalServerDemoRESTController.class);

  @Autowired WorkflowClient client;

  @CrossOrigin(origins = "http://localhost:8080")
  @PostMapping("hello-world")  
  public ResponseEntity<String> helloWorld(@RequestBody Person person)  {
    logger.debug("Entered helloWorld controller method");
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

     HelloWorkflow workflow =
        client.newWorkflowStub(
            HelloWorkflow.class,
            WorkflowOptions.newBuilder()
                .setTaskQueue("HelloDemoTaskQueue")
                .setWorkflowId("HelloDemo"+ timeStamp)
                .build());

     return new ResponseEntity<>("\"" + workflow.sayHello(person) + "\"", HttpStatus.OK);
  }
}
