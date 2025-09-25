package com.Edulink.EdulinkServer.mapper;

import com.Edulink.EdulinkServer.dto.order.OrderResponseDTO;
import com.Edulink.EdulinkServer.dto.wallet.WalletResponseDTO;
import com.Edulink.EdulinkServer.model.Order;
import com.Edulink.EdulinkServer.model.Wallet;

import java.util.List;

public class WalletMapper {

    public static WalletResponseDTO walletResponseDTO (Wallet wallet, List<Order> orders){
        WalletResponseDTO dto = new WalletResponseDTO();
       dto.setWalletId(wallet.getWalletId());
       dto.setUserId(wallet.getWalletOwner().getUserId());
       dto.setBalance(wallet.getBalance());
       dto.setTotalEarnings(wallet.getTotalEarnings());
       dto.setWithdrawn(wallet.getWithdrawn());


        List<OrderResponseDTO> txList = orders.stream().map(order -> {
            OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
            orderResponseDTO.setAmount(order.getAmount());
            orderResponseDTO.setDate(order.getCreatedAt().toString());
            orderResponseDTO.setId(order.getId());
            orderResponseDTO.setStudentName(order.getStudent() != null ? order.getStudent().getFirstName() : "Unknown");
            orderResponseDTO.setStatus(order.getStatus());
            orderResponseDTO.setClassroomName(order.getClassroomName());
            orderResponseDTO.setClassroomPrice(order.getClassroomPrice());
            orderResponseDTO.setClassroomDescription(order.getClassroomDescription());
            return orderResponseDTO;
        }).toList();

        dto.setTransactions(txList);
       return dto;
    }
}
