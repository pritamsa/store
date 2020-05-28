package com.store.skeleton;

import com.store.services.webapi.datastore.Repository;
import com.store.services.webapi.errors.ApplicationError;
import com.store.services.webapi.models.Discount;
import com.store.services.webapi.models.Purchase;
import com.store.services.webapi.responses.DiscountResponse;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller with all the apis.
 */
@RestController
@RequestMapping(path = "/api")
public class BlogController {

    @Autowired
    Repository repository;

    static final Logger logger = Logger.getLogger(BlogController.class);

    BlogController() {
      BasicConfigurator.configure();
    }

  /**
   * Creates discount setting
   *
   * @param discount the discount setting
   * @param headers nothing specific
   * @return 500 if internal error. 201 for happy path
   */
    @PostMapping(
            path = "/discount",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity createDiscountSetting(@RequestHeader
        final HttpHeaders headers, @RequestBody final Discount discount)
        {

      try {
        repository.addNewDiscountSetting(discount);
      }catch (Exception e) {
        logger.error("Exception while creating discount", e);
        ApplicationError apiError = new ApplicationError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal "
            + "Error. Please try again later.", e);
        return buildResponseEntity(apiError);
      }

      return ResponseEntity.status(HttpStatus.CREATED).body("Discount Created");

    }

  /**
   * Creates a purchase transaction
   *
   * @param purchase the purchase
   * @param headers nothing specific
   * @return 500 if internal error. 201 for happy path
   */
  @PostMapping(
      path = "/purchase",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity createPurchase(@RequestHeader
  final HttpHeaders headers, @RequestBody final Purchase purchase) {

    ApplicationError apiError = null;

    try {
      if (!repository.isDiscountValid(purchase)) {
        apiError = new ApplicationError(HttpStatus.BAD_REQUEST, "You have entered incorrect "
            + "discount code or amount. "
            + "Please try the purchase with correct discount or without a discount");
        return buildResponseEntity(apiError);

      }
      if (purchase.isValid()) {
        repository.addCustomerPurchase(purchase);
      } else {
        apiError = new ApplicationError(HttpStatus.BAD_REQUEST, "You have entered an incorrect "
            + "purchase. Please try the purchase with a correct purchase amount and customer id.");
        return buildResponseEntity(apiError);

      }
    } catch (Exception e) {
      logger.error("Exception while creating purchase", e);
      apiError = new ApplicationError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal "
          + "Error. Please try again later.", e);
      return buildResponseEntity(apiError);

    }

    return ResponseEntity.status(HttpStatus.CREATED).body("Your purchase is successful");

  }

  /**
   * Reads total discounts
   *
   */

  @GetMapping(
      path = "/totaldiscounts",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity getAllDiscounts()  {

    Long totalDiscount = repository.getTotalDiscountsOffered("");
    return ResponseEntity.status(HttpStatus.OK).body("Total "
        + "discount offered:" + totalDiscount);
  }

  /**
   * Reads total discounts for a discount code.
   * @param discountCode : discount code
   */

  @GetMapping(
      path = "/totaldiscounts/{discountCode}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity getTotalDiscounts(@PathVariable final String discountCode)  {

      Long totalDiscount = repository.getTotalDiscountsOffered(discountCode);
      return ResponseEntity.status(HttpStatus.OK).body("DiscountCode:" + discountCode + " total "
          + "discount:" + totalDiscount);
  }

  /**
   * Reads total discounts for a customer id.
   * @param customerId : customer id
   */
  @GetMapping(
      path = "/discounts/{customerId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity getDiscounts(@PathVariable final String customerId)  {
      List<com.store.services.webapi.datastore.discount.Discount> lst = repository
          .getDiscounts(customerId);


    if (lst == null || lst.isEmpty()) {
      ApplicationError apiError = new ApplicationError(HttpStatus.OK, "This customer has not "
          + "earned any discount");
      return buildResponseEntity(apiError);

    }
    DiscountResponse response = new DiscountResponse();
    response.setCustomerId(customerId);
    response.setDiscountList(lst);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  private ResponseEntity<Object> buildResponseEntity(ApplicationError apiError) {
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }


}
