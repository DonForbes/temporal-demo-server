package com.donald.demo.temporaldemoserver.customize;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.temporal.client.WorkflowClientOptions;
import io.temporal.common.converter.CodecDataConverter;
import io.temporal.common.converter.DefaultDataConverter;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.spring.boot.TemporalOptionsCustomizer;
import io.temporal.spring.boot.WorkerOptionsCustomizer;
import io.temporal.worker.WorkerFactoryOptions;
import io.temporal.worker.WorkerOptions;
import io.temporal.worker.WorkflowImplementationOptions;
import jakarta.annotation.Nonnull;
import java.util.Collections;

import com.donald.demo.temporaldemoserver.util.CryptCodec;

@Configuration
public class TemporalOptionsConfig {
  @Bean
  public WorkerOptionsCustomizer customWorkerOptions() {
    return new WorkerOptionsCustomizer() {
      @Nonnull
      @Override
      public WorkerOptions.Builder customize(
          @Nonnull WorkerOptions.Builder optionsBuilder,
          @Nonnull String workerName,
          @Nonnull String taskQueue) {

        // For CustomizeTaskQueue (also name of worker) we set worker
        // to only handle workflow tasks and local activities
  //      if (taskQueue.equals("CustomizeTaskQueue")) {
  //        optionsBuilder.setLocalActivityWorkerOnly(true);
   //     }
        return optionsBuilder;
      }
    };
  }

  // WorkflowServiceStubsOptions customization
  @Bean
  public TemporalOptionsCustomizer<WorkflowServiceStubsOptions.Builder>
      customServiceStubsOptions() {
    return new TemporalOptionsCustomizer<WorkflowServiceStubsOptions.Builder>() {
      @Nonnull
      @Override
      public WorkflowServiceStubsOptions.Builder customize(
          @Nonnull WorkflowServiceStubsOptions.Builder optionsBuilder) {
        // set options on optionsBuilder as needed
        // ...
        return optionsBuilder;
      }
    };
  }

  // WorkflowClientOption customization
  // Only option we are adding just now is for the codec converter.
  @Bean
  public TemporalOptionsCustomizer<WorkflowClientOptions.Builder> customClientOptions() {
    return new TemporalOptionsCustomizer<WorkflowClientOptions.Builder>() {
      @Nonnull
      @Override
      public WorkflowClientOptions.Builder customize(
          @Nonnull WorkflowClientOptions.Builder optionsBuilder) {
 
        optionsBuilder.setDataConverter(
            new CodecDataConverter(
                DefaultDataConverter.newDefaultInstance(), 
                Collections.singletonList(new CryptCodec()),
                true ) 
            );
        return optionsBuilder;
      }
    };
  }

  // WorkerFactoryOptions customization
  @Bean
  public TemporalOptionsCustomizer<WorkerFactoryOptions.Builder> customWorkerFactoryOptions() {
    return new TemporalOptionsCustomizer<WorkerFactoryOptions.Builder>() {
      @Nonnull
      @Override
      public WorkerFactoryOptions.Builder customize(
          @Nonnull WorkerFactoryOptions.Builder optionsBuilder) {
        // set options on optionsBuilder as needed
        // ...
        return optionsBuilder;
      }
    };
  }

  // WorkflowImplementationOptions customization
  @Bean
  public TemporalOptionsCustomizer<WorkflowImplementationOptions.Builder>
      customWorkflowImplementationOptions() {
    return new TemporalOptionsCustomizer<>() {
      @Nonnull
      @Override
      public WorkflowImplementationOptions.Builder customize(
          @Nonnull WorkflowImplementationOptions.Builder optionsBuilder) {
        // set options on optionsBuilder such as per-activity options
        return optionsBuilder;
      }
    };
  }
}

