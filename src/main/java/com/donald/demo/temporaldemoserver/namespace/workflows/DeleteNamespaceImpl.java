package com.donald.demo.temporaldemoserver.namespace.workflows;

import com.donald.demo.temporaldemoserver.namespace.activities.NamespaceManagement;
import com.donald.demo.temporaldemoserver.namespace.model.CloudOperationsNamespace;
import com.donald.demo.temporaldemoserver.namespace.model.WorkflowMetadata;

import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import java.time.Duration;

import org.slf4j.Logger;

@WorkflowImpl
public class DeleteNamespaceImpl implements DeleteNamespace{
    public static final Logger logger = Workflow.getLogger(DeleteNamespaceImpl.class);

    private CloudOperationsNamespace cloudOpsNamespace = new CloudOperationsNamespace();
    private WorkflowMetadata wfMetadata;


    private NamespaceManagement namespaceManagement = Workflow.newActivityStub(NamespaceManagement.class,
                            ActivityOptions.newBuilder()
                                           .setStartToCloseTimeout(Duration.ofSeconds(30))
                                           .build());

    @Override
    public String deleteNamespace( WorkflowMetadata pWFMetadata, CloudOperationsNamespace pCloudOpsNamespace) {
        wfMetadata = pWFMetadata;

        cloudOpsNamespace = namespaceManagement.getExistingNamespace(pCloudOpsNamespace, wfMetadata.getApiKey());

        // Set data gathered to indicate to UI that we have queried the API and have the data, either initially for the CA only 
        // or for all existing details.
        wfMetadata.setNsDataGathered(true);
        wfMetadata.setApproved(false);

        while (!wfMetadata.getApproved())
        {

          Workflow.await(Duration.ofMinutes(wfMetadata.getManageNamespaceTimeoutMins()), () -> wfMetadata.getApproved());
          if (wfMetadata.getApproved())
          {
            // Approval for delete has been received
            logger.debug("Deleting namespace [{}]", cloudOpsNamespace.getName());

            namespaceManagement.deleteNamespace(cloudOpsNamespace, wfMetadata.getApiKey());

        
          }
          else
          {  
            // Break out of the loop.
            logger.debug("The timer fired with no approval so we are completing the workflow with no action taken.");
            return "Timed out waiting for approval.  Not deleting namespace [" +  cloudOpsNamespace.getName() + "]";
          }
        }

        logger.debug("Mailing out to users [{}]", namespaceManagement.emailChanges(cloudOpsNamespace));
        
        return "Successful deletion of namespace";
   
    }
    @Override
    public CloudOperationsNamespace getNamespaceDetails() {
        return cloudOpsNamespace;
    }
    @Override
    public WorkflowMetadata getWFMetadata() {
        logger.debug("Returning Metadata to query - [{}]", wfMetadata.toString());
        return wfMetadata;
    }
    @Override
    public void setApproved() {
       wfMetadata.setApproved(true);
    }

}
