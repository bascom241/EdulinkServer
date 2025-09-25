package com.Edulink.EdulinkServer.controller;

import com.Edulink.EdulinkServer.dto.wallet.WalletResponseDTO;
import com.Edulink.EdulinkServer.model.Wallet;
import com.Edulink.EdulinkServer.repository.WalletRepository;
import com.Edulink.EdulinkServer.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @GetMapping("/user-wallet/{userId}")
    public ResponseEntity<?> getSingleWallet(@PathVariable(name = "userId") Long userId){

        try {
            WalletResponseDTO userWallet = walletService.getWallet(userId);
            if (userWallet == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wallet Not found");

            return ResponseEntity.status(HttpStatus.OK).body(userWallet);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
