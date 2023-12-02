package com.superngb.boardservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "card-service", url = "${feign_client.card_service.url}")
public interface CardServiceClient {
    @DeleteMapping("/deleteByBoard/{id}")
    void deleteCardsByBoard(@PathVariable Long id);
}