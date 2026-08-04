package com.saksham.Ecommerce.controller;

import com.saksham.Ecommerce.config.JwtProvider;
import com.saksham.Ecommerce.domain.AccountStatus;
import com.saksham.Ecommerce.entity.Seller;
import com.saksham.Ecommerce.entity.SellerReports;
import com.saksham.Ecommerce.entity.VerificationCode;
import com.saksham.Ecommerce.repository.VerificationCodeRepository;
import com.saksham.Ecommerce.request.LoginRequest;
import com.saksham.Ecommerce.response.ApiResponse;
import com.saksham.Ecommerce.response.AuthResponse;
import com.saksham.Ecommerce.service.AuthService;
import com.saksham.Ecommerce.service.EmailService;
import com.saksham.Ecommerce.service.SellerService;
import com.saksham.Ecommerce.utils.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {
    private final SellerService sellerService;
    private final VerificationCodeRepository  verificationCodeRepository;
    private final AuthService authService;
    private final EmailService emailService;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest request) throws Exception {
        String otp = request.getOtp();
        String email = request.getEmail();

        request.setEmail("seller_" +email); //because this is a Seller we have to add seller as a prefix otherwise seller account will consider as a normal user account
        AuthResponse authResponse = authService.signing(request);

        return ResponseEntity.ok(authResponse);
    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception {
        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);
        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("Wrong otp");
        }

        Seller seller = sellerService.verifySellerEmail(verificationCode.getEmail(), otp);

        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception, MessagingException {

        Seller savedSeller = sellerService.createSeller(seller);

        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);


        String subject = "Ecommerce Monolithic Backend";
        String text = "Welcome to Ecommerce with AI Backend Code";
        String frontend_url = "http://localhost:8080/verify-seller";
        emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject, text + frontend_url);
        return new ResponseEntity<>(savedSeller, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws Exception{
        Seller seller = sellerService.getSellerById(id);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

//    @GetMapping("/report")
//    public ResponseEntity<SellerReports> getSellerReport(@RequestHeader("Authorization")  String jwt) throws Exception {
//        String email = jwtProvider.getEmailFromJwtToken(jwt);
//        Seller seller = sellerService.getSellerByEmail(email);
//        SellerReports sellerReports = sellerReportService.getSellerRepost(seller);
//        return new ResponseEntity<>(sellerReports, HttpStatus.OK);
//    }

    @GetMapping()
    public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false)AccountStatus accountStatus) {
        List<Seller> sellers = sellerService.getAllSellersByAccountStatus(accountStatus);
        return new ResponseEntity<>(sellers, HttpStatus.OK);
    }

    @PatchMapping()
    public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt,
                                               @RequestBody Seller seller) throws Exception {
        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updateSeller = sellerService.updateSeller(profile.getId(), seller);
        return new ResponseEntity<>(updateSeller, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Seller> deleteSellerById(@PathVariable Long id) throws Exception {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }
}
