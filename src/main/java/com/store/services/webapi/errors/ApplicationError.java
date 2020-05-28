package com.store.services.webapi.errors;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * This class is used for api error messages.
 */
@Data
public class ApplicationError {
  private HttpStatus status;

  private String message;
  private String debugMessage = "";


  private ApplicationError() {

  }

  public ApplicationError(HttpStatus status) {
    this();
    this.status = status;
  }

  public ApplicationError(HttpStatus status, String message) {
    this();
    this.status = status;
    this.message = message;
  }

  public ApplicationError(HttpStatus status, Throwable ex) {
    this();
    this.status = status;
    this.message = "Unexpected error";
    this.debugMessage = ex.getLocalizedMessage();
  }

  public ApplicationError(HttpStatus status, String message, Throwable ex) {
    this();
    this.status = status;
    this.message = message;
    this.debugMessage = ex.getLocalizedMessage();
  }

}
