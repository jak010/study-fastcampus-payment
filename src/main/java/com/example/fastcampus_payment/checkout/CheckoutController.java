package com.example.fastcampus_payment.checkout;


import com.example.fastcampus_payment.order.Order;
import com.example.fastcampus_payment.order.OrderRepository;
import com.example.fastcampus_payment.payement_processing.PaymentProcessingService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@AllArgsConstructor
public class CheckoutController {
    private final OrderRepository orderRepository;
    private final PaymentProcessingService paymentProcessingService;

    @GetMapping("/order")
    public String order(
        @RequestParam("userId") Long userId,
        @RequestParam("courseId") Long courseId,
        @RequestParam("courseName") String courseName,
        @RequestParam("amount") String amount,
        Model model
    ) {
        Order order = new Order();
        order.setAmount(new BigDecimal(amount));
        order.setCourseId(courseId);
        order.setCourseName(courseName);
        order.setUserId(userId);
        order.setRequestId(UUID.randomUUID().toString());
        order.setStatus(Order.Status.WAIT);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        model.addAttribute("courseName", courseName);
        model.addAttribute("requestId", order.getRequestId());
        model.addAttribute("amount", amount);
        model.addAttribute("customerKey", "customerKey-" + userId);
        return "/order.html";
    }

    @GetMapping("/order-requested")
    public String orderRequested() {
        return "/order-requested.html";
    }

    @GetMapping("/fail")
    public String fail() {
        return "/fail.html";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/confirm")
    public ResponseEntity<Object> confirmPayment(
        @RequestBody ConfirmRequest confirmRequest) throws Exception {
        // 1번
        Order order = orderRepository.findByRequestId(confirmRequest.orderId());
        order.setUpdatedAt(LocalDateTime.now());
        order.setStatus(Order.Status.REQUESTED);
        orderRepository.save(order);

        // 2번.
        paymentProcessingService.createPayment(confirmRequest);


        return ResponseEntity.ok(null);
    }
}
