package com.donald.demo.temporaldemoserver.namespace.model;

import lombok.Data;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Collection;

@Data
public class CloudOperationsCertAuthority {
  private String caCert;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private Date expiryDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private Date notBefore;
  private String subjectPrincipal;
  private Collection<String> alternativeNames;
}
