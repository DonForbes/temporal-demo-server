package com.donald.demo.temporaldemoserver.namespace.model;

import lombok.Data;
import java.util.Collection;

@Data
public class CloudOperationsNamespace {
    private String name;
    private String activeRegion;
    //private String secondaryRegion;
    private String state;
    private int retentionPeriod;
    private String certAuthorityPublicCert;  // Base64 encoded
    private Collection<CloudOperationsUser> cloudOpsUsers;
}
