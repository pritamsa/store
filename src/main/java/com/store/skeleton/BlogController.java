package com.store.skeleton;

import com.store.services.webapi.datastore.SingletonClass;
import com.store.services.webapi.datastore.discount.DiscountSetting;
import com.store.services.webapi.models.Discount;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/store")
public class BlogController {

    private DiscountSetting discountSetting;

    @RequestMapping(value = "/foos", method = RequestMethod.POST)
    @ResponseBody
    public String postFoos() {
        return "Post some Foos";
    }

    @RequestMapping(
            path = "/discount",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity createDiscountSetting(@RequestHeader
        final HttpHeaders headers, @RequestBody final Discount discount) {

        return null;

    }

    @RequestMapping("/")
    public String index() {
        if (SingletonClass.getInstance().val == 0) {
            SingletonClass.getInstance().val = 8;
        }
        SingletonClass.getInstance().val++;

        return "Congratulations from BlogController.java " + SingletonClass.getInstance().val;
    }
}
