/*
 * Response.java
 *
 * Created on Sep 7, 2006
 */
package org.soulwing.cas.client;


/**
 * A value class for the result to a CAS request.
 *
 * @author Carl Harris
 * 
 */
public class Response {

  private boolean successful;
  private String resultCode;
  private String resultMessage;
  
  /**
   * Gets the value of the success flag.
   * @return <code>true</code> if the validation representated by this
   *    response was successful.
   */
  public boolean isSuccessful() {
    return successful;
  }
  
  /**
   * Sets the value of the success flag.
   * @param successful success value to set.
   */
  public void setSuccessful(boolean successful) {
    this.successful = successful;
  }

  /**
   * Gets the result code for the response to an unsuccessful validation.
   * @return response code if <code>isSuccessful == true</code>, else
   *    <code>null</code>.
   */
  public String getResultCode() {
    return resultCode;
  }
  
  /**
   * Sets the result code for this response.
   * @param resultCode result code to set.
   */
  public void setResultCode(String resultCode) {
    this.resultCode = resultCode;
  }
  
  /**
   * Gets the result message for the response to an unsuccessful validation.
   * @return response message if <code>isSuccessful == true</code>, else
   *    <code>null</code>.
   */
  public String getResultMessage() {
    return resultMessage;
  }
  
  /**
   * Sets the result message for this response.
   * @param resultMessage result message to set.
   */
  public void setResultMessage(String resultMessage) {
    this.resultMessage = resultMessage;
  }
  
}
