package com.Edulink.EdulinkServer.service;

import com.Edulink.EdulinkServer.dto.wallet.WalletResponseDTO;
import com.Edulink.EdulinkServer.model.Order;
import com.Edulink.EdulinkServer.model.User;
import com.Edulink.EdulinkServer.model.Wallet;
import com.Edulink.EdulinkServer.repository.OrderRepository;
import com.Edulink.EdulinkServer.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.Edulink.EdulinkServer.mapper.WalletMapper.walletResponseDTO;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private OrderRepository orderRepository;
    public WalletResponseDTO getWallet(Long userId){
        Wallet wallet =  walletRepository.findByWalletOwner_UserId(userId).orElseThrow(()-> new RuntimeException("Wallet Not found"));

        List<Order> orders = orderRepository.findByClassroomOwner_UserId(userId);

        System.out.println("Orders found: " + orders.size());
        orders.forEach(o -> System.out.println(o.getId() + " " + o.getAmount()));



        return walletResponseDTO(wallet, orders);
    }
}
