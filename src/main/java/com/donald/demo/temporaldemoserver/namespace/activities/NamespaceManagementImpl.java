package com.donald.demo.temporaldemoserver.namespace.activities;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import com.donald.demo.temporaldemoserver.namespace.model.CloudOperations;
import com.donald.demo.temporaldemoserver.namespace.model.CloudOperationsNamespace;
import com.donald.demo.temporaldemoserver.namespace.model.CloudOperationsServerConfig;
import com.donald.demo.temporaldemoserver.namespace.model.WorkflowMetadata;
import com.donald.demo.temporaldemoserver.transfermoney.AccountTransferActivitiesImpl;

import io.temporal.spring.boot.ActivityImpl;

@Component
@ActivityImpl(taskQueues = "ManageNamespaceTaskQueue")
public class NamespaceManagementImpl implements NamespaceManagement {
    private static final Logger logger = LoggerFactory.getLogger(AccountTransferActivitiesImpl.class);
    @Autowired
    private CloudOperationsServerConfig cloudOpsServerConfig;

    @Override
    public CloudOperationsNamespace getExistingNamespace(CloudOperationsNamespace pCloudOpsNamespace, String apiKey) {
        // Method will query the cloudOps API to gather the namespace details for the
        // namespace identified in the parameter.
        URI uri = UriComponentsBuilder
                .fromUriString("{baseURI}/namespace/{namespace}")
                .queryParam("apiKey", "{apiKey}")
                .buildAndExpand(cloudOpsServerConfig.getBaseURI(), pCloudOpsNamespace.getName(), apiKey)
                .toUri();

        logger.debug("The URI to be used is[{}]", uri.toString());

        CloudOperationsNamespace cloudOpsNS = RestClient.create().get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    logger.info("Got an error back from operations.  Status[{}], Headers [{}]",
                            response.getStatusCode().toString(), response.getHeaders().toString());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    logger.info("Got an error back from operations.  Status[{}], Headers [{}]",
                            response.getStatusCode().toString(), response.getHeaders().toString());
                })
                .body(CloudOperationsNamespace.class);

        return cloudOpsNS;
    }

    @Override
    public String createNamespace(CloudOperationsNamespace pCloudOpsNamespace, String apiKey) {
        // Method will send the POST REST request to the cloud ops server to create the namespace.
        URI uri = UriComponentsBuilder
                .fromUriString("{baseURI}/namespace")
                .buildAndExpand(cloudOpsServerConfig.getBaseURI())
                .toUri();

        logger.debug("The URI to be used is[{}]", uri.toString());

        CloudOperations cloudOps = new CloudOperations();
        WorkflowMetadata wfMetadata = new WorkflowMetadata();
        wfMetadata.setApiKey(apiKey);
        cloudOps.setWfMetadata(wfMetadata);
        cloudOps.setCloudOpsNamespace(pCloudOpsNamespace);

        String returnString = "Successful Create";
        try {
        ResponseEntity<Void> response = RestClient.create().post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .body(cloudOps)
                .retrieve()
                .toBodilessEntity();
        }
        catch (RestClientException ex)
        {
            logger.debug("Failed to post the namespace to the Temporal Operations [{}] ", ex.getMessage());
            returnString = ex.getMessage();
        }


        return returnString;
    }

    @Override
    public String updateNamespace(CloudOperationsNamespace cloudOpsNamespace, String apiKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNamespace'");
    }
}
