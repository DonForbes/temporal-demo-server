package com.donald.demo.temporaldemoserver.namespace.workflows;

import org.slf4j.Logger;

import java.time.Duration;

import com.donald.demo.temporaldemoserver.hello.HelloWorkflowImpl;
import com.donald.demo.temporaldemoserver.namespace.model.CloudOperationsNamespace;
import com.donald.demo.temporaldemoserver.namespace.model.WorkflowMetadata;


import io.temporal.workflow.Workflow;

public class ManageNamespaceImpl implements ManageNamespace {
    public static final Logger logger = Workflow.getLogger(ManageNamespaceImpl.class);

    @Override
    public CloudOperationsNamespace manageNamespace(WorkflowMetadata wfMetadata, CloudOperationsNamespace pCloudOpsNamespace) {
        CloudOperationsNamespace cloudOpsNamespace = pCloudOpsNamespace;
        long lastModified = Workflow.currentTimeMillis();


        if (wfMetadata.getIsNewNamespace())
          logger.debug("Running workflow to create a new namespace.");

        // Keep workflow alive until it has remained untouched for more than the metadata duration time or it has completed successfully.
        while (System.currentTimeMillis() < lastModified + wfMetadata.getManageNamespaceTimeoutMins() * 60 * 1000) 
        {

            logger.debug("Waiting another minute before checking inactivity.");
            Workflow.sleep(Duration.ofMinutes(1));
        }

        return cloudOpsNamespace;
    }

}
